package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = CalenderPageViewModel.factory::class)
class CalenderPageViewModel @AssistedInject constructor(
    @Assisted val navKey: Screen.CalenderPageRoute,
    val habitRepository: HabitRepository
)
 : ViewModel() {
     @AssistedFactory
     interface factory{
         fun create(navKey: Screen.CalenderPageRoute): CalenderPageViewModel
     }
    init {
        getHabitData()
    }
    private val _habitData = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitData = _habitData.asStateFlow()
    fun getHabitData(){
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitEntriesById(navKey.habitId).collect { _habitData.value = it }
        }

    }
    fun addHabitEntry(habitId : Int , completionDate : LocalDate){
        viewModelScope.launch {
            habitRepository.addHabitCompletionEntry(HabitCompletionEntity(habitId = habitId, completionDate = completionDate))
        }
    }
    fun deleteEntryByDateAndHabitId(habitId : Int,date: Long){
        viewModelScope.launch (Dispatchers.IO){
        habitRepository.deleteHabitCompletionEntry(habitId, date)
    }}
}