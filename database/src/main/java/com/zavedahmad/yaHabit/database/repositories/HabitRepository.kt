package com.zavedahmad.yaHabit.database.repositories

import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    suspend fun addSampleHabits()
    suspend fun deleteAllHabits()
    suspend fun move(fromIndex: Int, toIndex: Int)
    suspend fun addHabitItem(habitEntity: HabitEntity) : Long
    fun editItem(habitEntity: HabitEntity)
    fun deleteHabit(id: Int)
    fun getHabitsFlowSortedByIndex(): Flow<List<HabitEntity>>
    suspend fun archive(id: Int)
    suspend fun unArchive(id: Int)

    suspend fun getHabitDetailsById(id: Int): HabitEntity
    suspend fun decreaseRepetitions(entryId: Int, newRepetitionValue: Double)
    suspend fun cleanUp(habitId: Int, startDate: LocalDate, endDate: LocalDate)
    suspend fun increaseRepetitions(entryId: Int, newRepetitionValue: Double)
    suspend fun applyRepetitionForADate(date: LocalDate, habitId: Int, newRepetitionValue: Double)
    suspend fun applyNotes(date: LocalDate, habitId: Int, newNote: String?)
    suspend fun setSkip(date: LocalDate, habitId: Int, skipValue: Boolean)
    suspend fun repairPartials(newHabitEntity: HabitEntity)
    suspend fun addHabitCompletionEntry(entry: HabitCompletionEntity)
    suspend fun deleteHabitCompletionEntry(habitId: Int, date: LocalDate)
    suspend fun getAllHabitCompletionsById(habitId: Int): List<HabitCompletionEntity>?
    fun getAllAbsoluteHabitCompletionsById(habitId: Int): List<HabitCompletionEntity>?
    fun getAllHabitCompletionsByIdFlow(id: Int): Flow<List<HabitCompletionEntity>?>
    fun getEntriesAfterDate(habitId: Int, completionDate: Long): Flow<List<HabitCompletionEntity>?>
}