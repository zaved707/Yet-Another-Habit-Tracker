package com.zavedahmad.yaHabit.roomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Upsert
    suspend fun addHabit(habitEntity: HabitEntity)

    @Query("SELECT * FROM HabitTable")
    fun getHabitsFlow(): Flow<List<HabitEntity>>

    @Query("DELETE FROM HabitTable WHERE id= :id")
    suspend fun deleteHabitById(id: Int)



}