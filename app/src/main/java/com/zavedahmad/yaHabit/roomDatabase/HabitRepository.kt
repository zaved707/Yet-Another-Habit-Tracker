package com.zavedahmad.yaHabit.roomDatabase

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class HabitRepository @Inject constructor(val habitDao: HabitDao, val habitCompletionDao: HabitCompletionDao) {
    suspend fun deleteHabit(id: Int) {
        habitDao.deleteHabitById(id)
    }

    suspend fun addItem(habitEntity: HabitEntity) {
        val max = habitDao.getMaxIndex()

        habitDao.addHabit(
            HabitEntity(
                name = habitEntity.name,
                color = habitEntity.color,
                index = max?.let { it + 1 } ?: 0
            )
        )

    }

    suspend fun editItem(habitEntity: HabitEntity) {
        habitDao.addHabit(
            habitEntity
        )
    }

    suspend fun move(fromIndex: Int, toIndex: Int) {
        val entity = habitDao.getHabitByIndex(fromIndex)
        habitDao.pluck(entity.index)
        habitDao.vacant(toIndex)
        habitDao.changeIndex(toIndex, entity.id)
    }

    fun getHabitsFlowSortedByIndex(): Flow<List<HabitEntity>> {
        return habitDao.getHabitsFlowSortedByIndex()
    }

    fun getAllHabitEntriesById(id: Int) : Flow<List<HabitCompletionEntity>?>{
        return habitCompletionDao.getHabitCompletionsById(id)
    }
    suspend fun getHabitDetailsById(id: Int) : HabitEntity{

           return habitDao.getHabitById(id)

    }

    suspend  fun addHabitCompletionEntry(entry : HabitCompletionEntity){

            habitCompletionDao.addHabitCompletionEntry(entry)

    }

    suspend fun deleteHabitCompletionEntry(habitId: Int, date : Long){
        habitCompletionDao.deleteHabitCompletionEntry(habitId = habitId, completionDate = date)
    }
}