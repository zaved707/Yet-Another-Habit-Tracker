package com.zavedahmad.yaHabit.roomDatabase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepository @Inject constructor(val habitDao: HabitDao) {
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
    suspend fun move(fromIndex : Int, toIndex : Int){
        val entity = habitDao.getHabitByIndex(fromIndex)
        habitDao.pluck(entity.index)
        habitDao.vacant(toIndex)
        habitDao.changeIndex(toIndex, entity.id)
    }
    fun getHabitsFlowSortedByIndex() :  Flow<List<HabitEntity>>{
        return habitDao.getHabitsFlowSortedByIndex()
    }
}