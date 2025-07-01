package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel(assistedFactory = CalenderPageViewModel.Factory::class)
class CalenderPageViewModel @AssistedInject constructor(
    @Assisted val navKey: Screen.CalenderPageRoute,
    val habitRepository: HabitRepository,
    val preferencesDao: PreferencesDao
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(navKey: Screen.CalenderPageRoute): CalenderPageViewModel
    }



    private val _habitData = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitData = _habitData.asStateFlow()

    private val _habitObject = MutableStateFlow<HabitEntity?>(null)
    val habitObject = _habitObject.asStateFlow()

    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()


    init {
        collectThemeMode()
        getHabitDetails()

        getHabitData()
    }
    fun getHabitData() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitEntriesById(navKey.habitId).collect { _habitData.value = it }
        }

    }

    fun getHabitDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _habitObject.value = habitRepository.getHabitDetailsById(navKey.habitId)
//            println(habitObject.value)
        }
    }

    fun addHabitEntry(habitId: Int, completionDate: LocalDate) {
        viewModelScope.launch {
            habitRepository.addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitId,
                    completionDate = completionDate
                )
            )
        }
    }

    fun deleteEntryByDateAndHabitId(habitId: Int, date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.deleteHabitCompletionEntry(habitId, date)
        }
    }
    fun collectThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("ThemeMode").collect { preference ->
                _themeMode.value = preference ?: PreferenceEntity("ThemeMode", "system")
            }
        }
    }
}