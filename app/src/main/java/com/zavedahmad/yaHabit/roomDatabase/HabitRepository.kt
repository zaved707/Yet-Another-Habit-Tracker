package com.zavedahmad.yaHabit.roomDatabase

import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.flow.Flow
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
        /*todo 1. get the habitEntity associated with the entry */
        val habitEntity = habitDao.getHabitById(entry.habitId)
        /*  todo 2. get all entries from database  where date is date of entry.date + habitEntity.frequency -1 and entry.date - habitEntity.frequency -1 */
        val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
            habitEntity.id,
            entry.completionDate.minusDays((habitEntity.cycle - 1).toLong()),
            entry.completionDate.plusDays((habitEntity.cycle - 1).toLong())
        )
        val partialEntries = entries?.filter { entry -> entry.partial == true }
        val absoluteEntries = entries?.filter { entry -> entry.partial != true }
        val currentEntryPresentInAbsolute =absoluteEntries?.any { it.completionDate == entry.completionDate } ?: false
        val currentEntryPresentInPartial =partialEntries?.any { it.completionDate == entry.completionDate } ?: false
        /*   todo 3. get clustersOfStreaks from those entries */
        val entriesList = absoluteEntries?.toMutableList() ?: mutableListOf()
        entriesList.add(entry)

            val clusters = findHabitClusters(entriesList, habitEntity.cycle, habitEntity.frequency)
            /*    todo 4. if clusters are there then for each cluster extract the dates*/
            val processedClusters = processDateTriples(clusters).toMutableList()

            val datesInLimitOfCycleOfDateBeingAdded = processedClusters.filter { date -> date < entry.completionDate.plusDays(habitEntity.cycle.toLong()-1   ) && date < entry.completionDate.minusDays(habitEntity.cycle.toLong()-1 ) }
            /* todo 5 for each day in the extracted dates from clusters if  entry does already exists in the entries then add a entry for that date with partial = true */
            val datesNotPresentInDataBase =
                datesInLimitOfCycleOfDateBeingAdded.filter { date -> !entriesList.any { it.completionDate == date } && !(partialEntries?.any { it.completionDate == date } ?: true) }
            /* todo now add all datesNotPresentIndDatabase to database as partial Entries*/
            datesNotPresentInDataBase.forEach { date ->
                addHabitCompletionEntry(
                    HabitCompletionEntity(
                        habitId = habitEntity.id,
                        completionDate = date,
                        partial = true
                    )
                )
            }
            /*todo then add the entry as non partial*/
            println(datesNotPresentInDataBase.size)
        /*todo add a condition where current Entry is partial. in that case turn it to absolute*/
        if (!currentEntryPresentInAbsolute && !currentEntryPresentInPartial) {
            addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitEntity.id,
                    completionDate = entry.completionDate
                )
            )
        }
    }

    suspend fun addHabitCompletionEntry(entry: HabitCompletionEntity) {
        habitCompletionDao.addHabitCompletionEntry(entry)
    }

    suspend fun deleteHabitCompletionEntry(habitId: Int, date: Long) {
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