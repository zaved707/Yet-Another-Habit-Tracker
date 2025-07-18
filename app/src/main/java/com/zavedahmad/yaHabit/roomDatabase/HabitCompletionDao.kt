package com.zavedahmad.yaHabit.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitCompletionDao {

    @Insert
    suspend fun addHabitCompletionEntry(habitCompletionEntity: HabitCompletionEntity)

    @Query("SELECT * FROM habitCompletion WHERE habitId = :id")
    fun getHabitCompletionsByIdFlow(id : Int): Flow<List<HabitCompletionEntity>?>

    @Query("SELECT * FROM habitCompletion WHERE habitId = :id")
    fun getHabitCompletionsById(id : Int): List<HabitCompletionEntity>?
    @Query("SELECT * FROM habitCompletion WHERE habitId = :id AND partial = 0")
    fun getAbsoluteHabitCompletionsById(id : Int): List<HabitCompletionEntity>?
    @Query("SELECT * FROM habitCompletion WHERE completionDate = :date")
    fun getHabitCompletionsByDate(date : Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate >= :startDate AND completionDate <= :endDate ")
    suspend fun getHabitsInDateRangeOfaCertainHabitId(habitId : Int, startDate: LocalDate, endDate: LocalDate): List<HabitCompletionEntity>?
    @Query("DELETE FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate")
    suspend fun deleteHabitCompletionEntry(habitId: Int, completionDate: LocalDate)

    @Query("DELETE FROM habitCompletion WHERE habitId = :habitId AND partial = 1")
    suspend fun deleteAllPartialFromId(habitId :Int)
    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate > :completionDate" )
    fun getEntriesAfterDate(habitId: Int, completionDate : Long): Flow<List<HabitCompletionEntity>?>
}