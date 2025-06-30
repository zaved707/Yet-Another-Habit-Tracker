package com.zavedahmad.yaHabit.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    @Insert
    suspend fun addHabitCompletionEntry(habitCompletionEntity: HabitCompletionEntity)

    @Query("SELECT * FROM habitCompletion WHERE habitId = :id")
    fun getHabitCompletionsById(id : Int): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habitCompletion WHERE completionDate = :date")
    fun getHabitCompletionsByDate(date : Long): Flow<List<HabitCompletionEntity>>

    @Query("DELETE FROM habitCompletion WHERE habitId = :habitId AND completionDate = :completionDate")
    suspend fun deleteHabitCompletionEntry(habitId: Int, completionDate: Long)

    @Query("SELECT * FROM habitCompletion WHERE habitId = :habitId AND completionDate > :completionDate" )
    fun getEntriesAfterDate(habitId: Int, completionDate : Long): Flow<List<HabitCompletionEntity>?>
}