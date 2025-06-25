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
    fun getHabitCompletions(id : Int): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habitCompletion WHERE completionDate = :date")
    fun getHabitCompletionsByDate(date : Long): Flow<List<HabitCompletionEntity>>
}