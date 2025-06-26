package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitPageViewModel @Inject constructor(val habitDao: HabitDao) : ViewModel() {

    private val _selectedColor = MutableStateFlow<Color>( Color(0xFFFF69B4))
    val selectedColor = _selectedColor.asStateFlow()

    fun setColor(color: Color){
        _selectedColor.value = color
    }
    fun addHabit(habitName : String){
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.addHabit(HabitEntity(name = habitName))
        }
    }

}