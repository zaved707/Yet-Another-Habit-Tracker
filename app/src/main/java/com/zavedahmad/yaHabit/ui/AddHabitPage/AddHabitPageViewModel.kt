package com.zavedahmad.yaHabit.ui.AddHabitPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitPageViewModel @Inject constructor(val habitDao: HabitDao) : ViewModel() {
    fun addHabit(habitName : String){
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.addHabit(HabitEntity(name = habitName))
        }
    }
}