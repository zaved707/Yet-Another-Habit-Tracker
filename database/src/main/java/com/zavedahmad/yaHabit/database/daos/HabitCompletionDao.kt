package com.zavedahmad.yaHabit.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitCompletionDao {

    @Upsert
    suspend fun addHabitCompletionEntry(habitCompletionEntity: HabitCompletionEntity) : Long
    @Query("SELECT EXISTS(SELECT 1 FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate)")
    suspend fun checkIfEntryForCertainHabitForACertainDayExists(habitId: Int, completionDate : LocalDate) : Boolean
    @Query("SELECT * FROM habitCompletion WHERE habitId = :id")
    fun getHabitCompletionsByIdFlow(id : Int): Flow<List<HabitCompletionEntity>>


    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate")
    suspend fun getEntryOfCertainHabitIdAndDate(habitId: Int, completionDate: LocalDate) : HabitCompletionEntity?

    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate")
    fun getEntryOfCertainHabitIdAndDateFlow(habitId: Int, completionDate: LocalDate) : Flow<HabitCompletionEntity?>
    @Query("SELECT * FROM habitCompletion WHERE habitId = :id")
    fun getHabitCompletionsById(id : Int): List<HabitCompletionEntity>
    @Query("SELECT * FROM habitCompletion WHERE habitId = :id AND partial = 0")
    fun getAbsoluteHabitCompletionsById(id : Int): List<HabitCompletionEntity>
    @Query("SELECT * FROM habitCompletion WHERE completionDate = :date")
    fun getHabitCompletionsByDate(date : Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habitCompletion WHERE id = :entryId")
    suspend fun getEntryById(entryId : Int) : HabitCompletionEntity?

    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate >= :startDate AND completionDate <= :endDate ")
    suspend fun getHabitsInDateRangeOfaCertainHabitId(habitId : Int, startDate: LocalDate, endDate: LocalDate): List<HabitCompletionEntity>
    @Query("DELETE FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate")
    suspend fun deleteHabitCompletionEntry(habitId: Int, completionDate: LocalDate)

    @Query("DELETE FROM habitCompletion WHERE id = :id")
    suspend fun deleteHabitCompletionEntryById(id: Int)


    @Query("DELETE FROM habitCompletion WHERE habitId = :habitId AND partial = 1")
    suspend fun deleteAllPartialFromId(habitId :Int)
    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate > :completionDate" )
    fun getEntriesAfterDate(habitId: Int, completionDate : Long): Flow<List<HabitCompletionEntity>>
}