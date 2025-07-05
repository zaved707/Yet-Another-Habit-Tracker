package com.zavedahmad.yaHabit.roomDatabase

import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class HabitRepository @Inject constructor(
    val habitDao: HabitDao,
    val habitCompletionDao: HabitCompletionDao
) {
    // HabitDao functions
    // Write operations
    suspend fun move(fromIndex: Int, toIndex: Int) {
        val entity = habitDao.getHabitByIndex(fromIndex)
        habitDao.pluck(entity.index)
        habitDao.vacant(toIndex)
        habitDao.changeIndex(toIndex, entity.id)
    }


    suspend fun addItem(habitEntity: HabitEntity) {
        val max = habitDao.getMaxIndex()
        habitDao.addHabit(
            HabitEntity(
                name = habitEntity.name,
                color = habitEntity.color,
                index = max?.let { it + 1 } ?: 0,
                cycle = habitEntity.cycle,
                frequency = habitEntity.frequency,
                streakType = habitEntity.streakType
            )
        )
    }

    suspend fun editItem(habitEntity: HabitEntity) {
        habitDao.addHabit(habitEntity)
    }

    suspend fun deleteHabit(id: Int) {
        habitDao.deleteHabitById(id)
    }

    // Read operations
    fun getHabitsFlowSortedByIndex(): Flow<List<HabitEntity>> {
        return habitDao.getHabitsFlowSortedByIndex()
    }

    suspend fun getHabitDetailsById(id: Int): HabitEntity {
        return habitDao.getHabitById(id)
    }

    // HabitCompletionDao functions
    // Write operations
    suspend fun addWithPartialCheck(entry: HabitCompletionEntity) {
        /* 1. get the habitEntity associated with the entry */
        val habitEntity = habitDao.getHabitById(entry.habitId)
        /*  2. get all entries from database  where date is date of entry.date + habitEntity.frequency -1 and entry.date - habitEntity.frequency -1 */
        val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
            habitEntity.id,
            entry.completionDate.minusDays((habitEntity.cycle - 1).toLong()),
            entry.completionDate.plusDays((habitEntity.cycle - 1).toLong())
        )
        val partialEntries = entries?.filter { entry -> entry.partial == true }
        val absoluteEntries = entries?.filter { entry -> entry.partial != true }
        val currentEntryPresentInAbsolute =
            absoluteEntries?.any { it.completionDate == entry.completionDate } ?: false
        val currentEntryPresentInPartial =
            partialEntries?.any { it.completionDate == entry.completionDate } ?: false
        /*    3. get clustersOfStreaks from those entries */
        val entriesList = absoluteEntries?.toMutableList() ?: mutableListOf()
        entriesList.add(entry)

        val clusters = findHabitClusters(entriesList, habitEntity.cycle, habitEntity.frequency)
        /*     4. if clusters are there then for each cluster extract the dates*/
        val processedClusters = processDateTriples(clusters).toMutableList()

        val datesInLimitOfCycleOfDateBeingAdded = processedClusters.filter { date ->

            date <= entry.completionDate.plusDays(habitEntity.cycle.toLong() - 1) && date >= entry.completionDate.minusDays(
                habitEntity.cycle.toLong() - 1
            )
        }
        /*  5 for each day in the extracted dates from clusters if  entry does already exists in the entries then add a entry for that date with partial = true */
        val datesNotPresentInDataBase =
            datesInLimitOfCycleOfDateBeingAdded.filter { date ->
                !entriesList.any { it.completionDate == date } && !(partialEntries?.any { it.completionDate == date }
                    ?: true)
            }
        /* now add all datesNotPresentIndDatabase to database as partial Entries*/
        datesNotPresentInDataBase.forEach { date ->
            addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitEntity.id,
                    completionDate = date,
                    partial = true
                )
            )
        }
        /*then add the entry as non partial*/
        println(datesNotPresentInDataBase.size)
        /* add a condition where current Entry is partial. in that case turn it to absolute*/
        if (currentEntryPresentInPartial) {
            habitCompletionDao.deleteHabitCompletionEntry(
                habitEntity.id,
                entry.completionDate
            )

        }

        if (!currentEntryPresentInAbsolute) {
            println("adding date to database")
            addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitEntity.id,
                    completionDate = entry.completionDate
                )
            )
        }

    }

    suspend fun deleteWithPartialCheck(entry: HabitCompletionEntity) {
        // todo extract cycle from 2*cycle in past and future of the entry.completionDate ,
        val habitEntity = habitDao.getHabitById(entry.habitId)

        val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
            habitEntity.id,
            entry.completionDate.minusDays((habitEntity.cycle * 2 - 2).toLong()),
            entry.completionDate.plusDays((habitEntity.cycle * 2 - 2).toLong())
        )
        println(entries?.size)
        // todo seperate in partial and absolute lists
        val partialEntries = entries?.filter { entry -> entry.partial == true }
        val absoluteEntries = entries?.filter { entry -> entry.partial != true }

        // todo remove entry.completiondate item from Absolute List
        val absoluteWithoutCurrentDay =
            absoluteEntries?.filter { item -> item.completionDate != entry.completionDate }

        // todo get clusters of the new absolute List and get their dates
        val clusters = findHabitClusters(
            absoluteWithoutCurrentDay ?: mutableListOf(),
            habitEntity.cycle,
            minOccurrences = habitEntity.frequency
        )

        val processedClusters = processDateTriples(clusters)

        processedClusters
        //todo check the items which are present in all extracted list from db. and are not present in the new

        val entriesToPurge = partialEntries?.filter { item -> item.completionDate !in processedClusters }


        // todo remove those entries from db
        entriesToPurge?.forEach { deleteHabitCompletionEntry(it.habitId, it.completionDate) }
        // remove the current date item
        deleteHabitCompletionEntry(habitEntity.id, entry.completionDate)
        // checking if current entity will change to partial or not
        if (entry.completionDate in processedClusters){
            addHabitCompletionEntry(HabitCompletionEntity(habitId =  habitEntity.id , completionDate = entry.completionDate, partial = true))
        }

    }
    // No checks
    suspend fun addHabitCompletionEntry(entry: HabitCompletionEntity) {
        habitCompletionDao.addHabitCompletionEntry(entry)
    }

    suspend fun deleteHabitCompletionEntry(habitId: Int, date: LocalDate) {
        habitCompletionDao.deleteHabitCompletionEntry(habitId = habitId, completionDate = date)
    }

    // Read operations
    fun getAllHabitEntriesById(id: Int): Flow<List<HabitCompletionEntity>?> {
        return habitCompletionDao.getHabitCompletionsById(id)
    }


    fun getEntriesAfterDate(
        habitId: Int,
        completionDate: Long
    ): Flow<List<HabitCompletionEntity>?> {
        return habitCompletionDao.getEntriesAfterDate(
            habitId = habitId,
            completionDate = completionDate
        )
    }
}