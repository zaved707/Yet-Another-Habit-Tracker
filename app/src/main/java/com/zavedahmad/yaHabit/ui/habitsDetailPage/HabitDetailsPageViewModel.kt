package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionDao
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = HabitDetailsPageViewModel.Factory::class)
class HabitDetailsPageViewModel @AssistedInject constructor(
    @Assisted val navKey: Screen.HabitDetailsPageRoute,
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao

) : ViewModel(){
    private val _habitsPastYear = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitsPastYear: StateFlow<List<HabitCompletionEntity>?> = _habitsPastYear
    @AssistedFactory
    interface Factory{
        fun create(navKey: Screen.HabitDetailsPageRoute): HabitDetailsPageViewModel
    }
    init {
        getLastYearData()
    }
    fun getLastYearData(){
        val dateNow = LocalDate.now()
        val dateAYearAgo = dateNow.minusYears(1).toEpochDay()
        viewModelScope.launch(Dispatchers.IO) {
            _habitsPastYear.value = habitCompletionDao.getEntriesAfterDate(navKey.habitId, dateAYearAgo)
        }
    }

}