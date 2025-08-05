package com.zavedahmad.yaHabit.roomDatabase

import androidx.compose.ui.graphics.Color
import androidx.room.withTransaction
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.entities.isAbsolute
import com.zavedahmad.yaHabit.database.entities.isDeletable
import com.zavedahmad.yaHabit.database.entities.isOnlyNote
import com.zavedahmad.yaHabit.database.entities.isPartial
import com.zavedahmad.yaHabit.database.entities.isSkip
import com.zavedahmad.yaHabit.database.entities.onlyPartial
import com.zavedahmad.yaHabit.database.enums.HabitStreakType
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlin.random.Random
import javax.inject.Inject

class HabitRepository @Inject constructor(
    val habitDao: HabitDao,
    val habitCompletionDao: HabitCompletionDao,
    val db: MainDatabase
) {
    suspend fun addSampleHabits() {
        val listOfHabits = listOf<HabitEntity>(
            HabitEntity(
                name = "Running",
                color = Color.Red,
                streakType = HabitStreakType.WEEKLY,
                frequency = 15.0,
                cycle = 7,
                repetitionPerDay = 5.0,
                measurementUnit = "Miles"
            ),
            HabitEntity(
                name = "Do Study",
                color = Color.White,
                streakType = HabitStreakType.DAILY,
                frequency = 7.0,
                cycle = 7,
                repetitionPerDay = 1.0,
                measurementUnit = "Hours"
            ),
            HabitEntity(
                name = "Read Books",
                color = Color.Green,
                streakType = HabitStreakType.DAILY,
                frequency = 5.0,
                cycle = 7,
                repetitionPerDay = 1.0,
                measurementUnit = "Pages"
            ),
            HabitEntity(
                name = "Exercise Daily",
                color = Color.Blue,
                streakType = HabitStreakType.DAILY,
                frequency = 7.0,
                cycle = 7,
                repetitionPerDay = 8.0,
                measurementUnit = "Glasses"
            ),
            HabitEntity(
                name = "Meditate",
                color = Color.Magenta,
                streakType = HabitStreakType.WEEKLY,
                frequency = 3.0,
                cycle = 7,
                repetitionPerDay = 1.0,
                measurementUnit = "Minutes"
            )
        )
        listOfHabits.forEach {  habit ->
        val habitId = habitDao.addHabit(
          habit
        )
        val todayDate = LocalDate.now()
        for (i in 1..300) {
            if (Random.nextDouble() < 0.7) {
                applyRepetitionForADate(
                    date = todayDate.minusDays(i.toLong()),
                    habitId = habitId.toInt(),
                    newRepetitionValue = 5.0
                )
            }
        }}

    }

    suspend fun deleteAllHabits(){
        habitDao.deleteAllHabits()
    }

    // HabitDao functions

    // Write operations

    suspend fun move(fromIndex: Int, toIndex: Int) {
        val entity = habitDao.getHabitByIndex(fromIndex)
        db.runInTransaction {
            runBlocking {
                habitDao.pluck(entity.index)
                habitDao.vacant(toIndex)
                habitDao.changeIndex(toIndex, entity.id)
            }
        }
    }


    suspend fun addHabitItem(habitEntity: HabitEntity) {
        val max = habitDao.getMaxIndex()
        habitDao.addHabit(
            HabitEntity(
                name = habitEntity.name,
                color = habitEntity.color,
                description = habitEntity.description,
                index = max?.let { it + 1 } ?: 0,
                cycle = habitEntity.cycle,
                frequency = habitEntity.frequency,
                streakType = habitEntity.streakType,
                measurementUnit = habitEntity.measurementUnit,
                repetitionPerDay = habitEntity.repetitionPerDay
            )
        )
    }

    suspend fun editItem(habitEntity: HabitEntity) {
        db.runInTransaction {
            runBlocking {
                habitDao.addHabit(habitEntity)
                repairPartials(habitEntity)
            }
        }
    }

    fun deleteHabit(id: Int) { // this deletes with index check
        db.runInTransaction {
            runBlocking {
                val habitEntity = getHabitDetailsById(id)
                // first pluck from this place
                habitDao.pluck(habitEntity.index)
                // then delete the habit
                habitDao.deleteHabitById(id)
            }
        }
    }

    // Read operations
    fun getHabitsFlowSortedByIndex(): Flow<List<HabitEntity>> {
        return habitDao.getHabitsFlowSortedByIndex()
    }

    suspend fun getHabitDetailsById(id: Int): HabitEntity {
        return habitDao.getHabitById(id)
    }


    suspend fun decreaseRepetitions(entryId: Int, newRepetitionValue: Double) {
        val entry = habitCompletionDao.getEntryById(entryId)

        entry?.let {
            val habitEntity = habitDao.getHabitById(entry.habitId)

            val modifiedCompletionEntity =
                entry.copy(partial = false, repetitionsOnThisDay = newRepetitionValue)

            // get -cycle and + cycle all habit Entries
            val dateToConsiderStart =
                entry.completionDate.minusDays(((habitEntity.cycle * 2) - 2).toLong())
            val dateToConsiderEnd =
                entry.completionDate.plusDays(((habitEntity.cycle * 2) - 2).toLong())
            val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
                habitId = habitEntity.id,
                startDate = dateToConsiderStart,
                endDate = dateToConsiderEnd
            )
            val partialEntries = entries?.filter { entry -> entry.onlyPartial() }
            val absoluteEntries =
                entries?.filter { entry -> entry.isAbsolute() }


            // swap the new value
            val entriesList = absoluteEntries?.toMutableList() ?: mutableListOf()
            entriesList.removeIf { it.completionDate == entry.completionDate }
            if (modifiedCompletionEntity.isAbsolute()) {
                entriesList.add(modifiedCompletionEntity)
            }
            // get clusters
            val clusters = findHabitClusters(
                habitEntries = entriesList,
                cycleLength = habitEntity.cycle,
                minOccurrences = habitEntity.frequency
            )

            // get processed clusters
            val processedClusters = processDateTriples(clusters)

            val datesInLimitOfCycleOfDateBeingAdded = processedClusters.filter { date ->

                date <= dateToConsiderEnd && date >= dateToConsiderStart
            }
            // seperate all the dates into alreadyPresent as Partial
            val partialsToRemove =
                partialEntries?.filter { partialEntry -> !(datesInLimitOfCycleOfDateBeingAdded.any { it == partialEntry.completionDate }) }


            val currentWillBePartial =
                if (newRepetitionValue == 0.0 && datesInLimitOfCycleOfDateBeingAdded.any { it == entry.completionDate }) {
                    true
                } else {
                    false
                }
            // for all present as notes turn their partial to true and add them to db
            partialsToRemove?.forEach {
                addHabitCompletionEntry(it.copy(partial = false))
            }
            if (currentWillBePartial) {
                addHabitCompletionEntry(modifiedCompletionEntity.copy(partial = true))
            } else {
                addHabitCompletionEntry(modifiedCompletionEntity)
            }
            cleanUp(habitEntity.id, startDate = dateToConsiderStart, endDate = dateToConsiderEnd)

        }
    }


    suspend fun cleanUp(habitId: Int, startDate: LocalDate, endDate: LocalDate) {
        val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
            habitId = habitId,
            startDate = startDate,
            endDate = endDate
        )
        entries?.forEach {
            if (it.isDeletable()) {
                habitCompletionDao.deleteHabitCompletionEntryById(it.id)
            }
        }

    }

    suspend fun increaseRepetitions(entryId: Int, newRepetitionValue: Double) {

        val entry = habitCompletionDao.getEntryById(entryId)
        entry?.let {
            val habitEntity = habitDao.getHabitById(entry.habitId)

            val modifiedCompletionEntity =
                entry.copy(partial = false, repetitionsOnThisDay = newRepetitionValue)
            // get -cycle and + cycle all habit Entries
            val dateToConsiderStart =
                entry.completionDate.minusDays((habitEntity.cycle - 1).toLong())
            val dateToConsiderEnd = entry.completionDate.plusDays((habitEntity.cycle - 1).toLong())

            val entries = habitCompletionDao.getHabitsInDateRangeOfaCertainHabitId(
                habitId = habitEntity.id,
                startDate = dateToConsiderStart,
                endDate = dateToConsiderEnd
            )
            // seperate HabitEntries
            val partialEntries = entries?.filter { entry -> entry.isPartial() }
            val absoluteEntries =
                entries?.filter { entry -> entry.isAbsolute() }
            val noteEntries = entries?.filter { it.isOnlyNote() }
            val skipEntries = entries?.filter { it.isSkip() }

            // swap the new value
            val entriesList = absoluteEntries?.toMutableList() ?: mutableListOf()
            entriesList.removeIf { it.completionDate == entry.completionDate }
            entriesList.add(modifiedCompletionEntity)

            // get clusters
            val clusters = findHabitClusters(
                habitEntries = entriesList,
                cycleLength = habitEntity.cycle,
                minOccurrences = habitEntity.frequency
            )

            // get processed clusters
            val processedClusters = processDateTriples(clusters)

            val datesInLimitOfCycleOfDateBeingAdded = processedClusters.filter { date ->

                date <= dateToConsiderEnd && date >= dateToConsiderStart
            }
            // seperate all the dates into alreadyPresent as Partial, alreadyPresentasNote and not present
            val presentAsPartials =
                partialEntries?.filter { partialEntry -> datesInLimitOfCycleOfDateBeingAdded.any { it == partialEntry.completionDate } }
            val presentAsNote =
                noteEntries?.filter { noteEntry -> datesInLimitOfCycleOfDateBeingAdded.any { it == noteEntry.completionDate } }
            val datesNotPresentInDataBase = datesInLimitOfCycleOfDateBeingAdded.filter { date ->
                (!entriesList.any { it.completionDate == date }) && !(partialEntries?.any { it.completionDate == date }
                    ?: true) && !(noteEntries?.any { it.completionDate == date }
                    ?: true) && !(skipEntries?.any { it.completionDate == date }
                    ?: true)
            }
            val presentAsSkip =
                skipEntries?.filter { skipEntry -> datesInLimitOfCycleOfDateBeingAdded.any { it == skipEntry.completionDate } }
            // for all present as notes turn their partial to true and add them to db
            presentAsNote?.forEach { addHabitCompletionEntry(it.copy(partial = true)) }
            presentAsSkip?.forEach { addHabitCompletionEntry(it.copy(partial = true)) }
            // add all non present in db as partials
            datesNotPresentInDataBase.forEach { date ->
                addHabitCompletionEntry(
                    HabitCompletionEntity(
                        habitId = habitEntity.id,
                        completionDate = date,
                        partial = true
                    )
                )
            }
            addHabitCompletionEntry(modifiedCompletionEntity)
            cleanUp(habitEntity.id, startDate = dateToConsiderStart, endDate = dateToConsiderEnd)

        }
    }


    suspend fun applyRepetitionForADate(date: LocalDate, habitId: Int, newRepetitionValue: Double) {
        db.withTransaction {
            val entry = habitCompletionDao.getEntryOfCertainHabitIdAndDate(
                habitId = habitId,
                completionDate = date
            )

            if (entry != null) {

                var type: String? = null
                if (newRepetitionValue == entry.repetitionsOnThisDay) {
                    // "equal"
                } else if (newRepetitionValue < entry.repetitionsOnThisDay) {
                    //"decrease"
                    decreaseRepetitions(entryId = entry.id, newRepetitionValue = newRepetitionValue)
                } else {
                    // "increase"
                    increaseRepetitions(entryId = entry.id, newRepetitionValue = newRepetitionValue)
                }
            } else {
                if (newRepetitionValue > 0.0) {
                    val newEntity = HabitCompletionEntity(
                        completionDate = date,
                        habitId = habitId,
                        repetitionsOnThisDay = newRepetitionValue
                    )

                    val newEntityId = habitCompletionDao.addHabitCompletionEntry(newEntity)
                    increaseRepetitions(
                        entryId = newEntityId.toInt(),
                        newRepetitionValue = newRepetitionValue
                    )
                }
            }
        }

    }

    // TODO add cleanup if item is empty to note entry addition
    suspend fun applyNotes(date: LocalDate, habitId: Int, newNote: String?) {


        val entry = habitCompletionDao.getEntryOfCertainHabitIdAndDate(
            habitId = habitId,
            completionDate = date
        )
        if (entry != null) {
            addHabitCompletionEntry(entry.copy(note = newNote))

        } else {
            addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitId,
                    completionDate = date,
                    note = newNote
                )
            )
        }


    }


    /**
     * @param date this is the date you for which skip status is set
     * @param habitId the habitId for which the skip value is set
     * @param skipValue the value of skip to set
     */
    suspend fun setSkip(date: LocalDate, habitId: Int, skipValue: Boolean) {

        db.withTransaction {
            val entry = habitCompletionDao.getEntryOfCertainHabitIdAndDate(
                habitId = habitId,
                completionDate = date
            )
            if (skipValue) {
                //decrease to zero
                // set skip to true
                if (entry != null) {
                    if (entry.repetitionsOnThisDay != 0.0) {
                        applyRepetitionForADate(
                            date = date,
                            habitId = habitId,
                            newRepetitionValue = 0.0
                        )
                        setSkip(
                            date = date,
                            habitId = habitId,
                            skipValue = skipValue
                        )
                    } else {
                        addHabitCompletionEntry(entry.copy(skip = true))
                    }
                } else {
                    addHabitCompletionEntry(
                        HabitCompletionEntity(
                            habitId = habitId,
                            completionDate = date,
                            skip = skipValue
                        )
                    )
                }
            } else {
                if (entry != null) {
                    if (entry.copy(skip = false).isDeletable()) {
                        habitCompletionDao.deleteHabitCompletionEntryById(entry.id)
                    } else {
                        addHabitCompletionEntry(entry.copy(skip = false))
                    }
                } else {
                    addHabitCompletionEntry(
                        HabitCompletionEntity(
                            habitId = habitId,
                            completionDate = date,
                            skip = skipValue
                        )
                    )
                }

            }
        }

    }

    // HabitCompletionDao functions
    // Write operations


    suspend fun repairPartials(newHabitEntity: HabitEntity) {

        //  get all absolute entries of the habit
        val habitAbsoluteEntries = getAllAbsoluteHabitCompletionsById(newHabitEntity.id)
        // get absolute clusters
        if (habitAbsoluteEntries != null) {
            val clusters = findHabitClusters(
                habitAbsoluteEntries,
                newHabitEntity.cycle,
                newHabitEntity.frequency
            )
            // parse clusters
            val processedClusters = processDateTriples(clusters)

            // from processed clusters remove all the absolute dates
            val removedAbsoluteFromProcessedDates =
                processedClusters.filter { it !in habitAbsoluteEntries.map { it -> it.completionDate } }

            // delete all partial from database
            habitCompletionDao.deleteAllPartialFromId(newHabitEntity.id)

            // add all the rest of dates as partials to database
            removedAbsoluteFromProcessedDates.forEach {
                addHabitCompletionEntry(
                    HabitCompletionEntity(
                        completionDate = it,
                        habitId = newHabitEntity.id,
                        partial = true
                    )
                )
            }

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
    suspend fun getAllHabitCompletionsById(habitId: Int): List<HabitCompletionEntity>? {
        return habitCompletionDao.getHabitCompletionsById(habitId)
    }

    fun getAllAbsoluteHabitCompletionsById(habitId: Int): List<HabitCompletionEntity>? {
        return habitCompletionDao.getAbsoluteHabitCompletionsById(habitId)
    }

    fun getAllHabitCompletionsByIdFlow(id: Int): Flow<List<HabitCompletionEntity>?> {
        return habitCompletionDao.getHabitCompletionsByIdFlow(id)
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