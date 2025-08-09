package com.zavedahmad.yaHabit.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @RawQuery
    suspend fun rawQuery(supportSQLiteQuery: SupportSQLiteQuery): Int
    @Upsert
    suspend fun addHabit(habitEntity: HabitEntity) : Long

    @Query("SELECT * FROM HabitTable WHERE id= :id")
    suspend fun getHabitById(id : Int): HabitEntity

    @Query("SELECT * FROM HabitTable WHERE id= :id")
    fun getHabitByIdFlow(id : Int): Flow<HabitEntity?>

    @Query("SELECT * FROM HabitTable")
    fun getHabitsFlow(): Flow<List<HabitEntity>>

    @Query("DELETE FROM HabitTable")
    suspend fun deleteAllHabits()

    @Query("SELECT * FROM HabitTable ORDER BY `index` ASC")
    fun  getHabitsFlowSortedByIndex(): Flow<List<HabitEntity>>

    @Query("UPDATE  HabitTable SET isArchived = :newValue WHERE id = :id")
    suspend fun archive(id: Int, newValue : Boolean)

    @Query("SELECT MAX(`index`) FROM HabitTable")
    suspend fun getMaxIndex(): Int?

    @Query("DELETE FROM HabitTable WHERE id= :id")
    suspend fun deleteHabitById(id: Int)

    @Query("UPDATE HabitTable SET `index` = `index` -1 WHERE `index` > :index")
    suspend fun pluck(index : Int)

    @Query("UPDATE HabitTable SET `index` = `index`+1 WHERE `index` >= :index")
    suspend fun vacant(index : Int)

    @Query("UPDATE HabitTable SET `index` = :index WHERE id = :id")
    suspend fun changeIndex(index : Int, id: Int)

    @Query("SELECT * FROM HabitTable WHERE `index`= :index")
    suspend fun getHabitByIndex(index : Int): HabitEntity


}