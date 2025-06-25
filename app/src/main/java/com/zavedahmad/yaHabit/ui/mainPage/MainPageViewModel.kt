package com.zavedahmad.yaHabit.ui.mainPage


import androidx.compose.runtime.LaunchedEffect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionDao
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class habitWithEntries(
    val habit : HabitEntity,
    val entries : List<HabitCompletionEntity>?
)
@HiltViewModel()
class MainPageViewModel @Inject constructor(
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao
) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }
    private val _listStartDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val listStartDate = _listStartDate.asStateFlow()
    private val _listEndDate = MutableStateFlow<LocalDate>(LocalDate.now().minusDays(30))
    val listEndDate = _listEndDate.asStateFlow()

    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits

    init {
        viewModelScope.launch(Dispatchers.IO) {
             habitDao.getHabitsFlow().collect { it -> _habits.value = it }
        }
    }

    fun addHabit(habitName : String){
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.addHabit(HabitEntity(name = habitName))
        }
    }
    fun addHabitEntry(habitId : Int , completionDate : LocalDate){
        viewModelScope.launch {
            habitCompletionDao.addHabitCompletionEntry(HabitCompletionEntity(habitId = habitId, completionDate = completionDate))
        }
    }
    fun readHabitEntries(){

    }
    fun getHabitCompletionsByDate(date: Long): Flow<List<HabitCompletionEntity>> {
        return habitCompletionDao.getHabitCompletionsByDate(date)
    }


}