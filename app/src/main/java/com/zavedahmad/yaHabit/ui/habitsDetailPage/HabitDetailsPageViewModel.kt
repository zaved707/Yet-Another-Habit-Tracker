package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionDao
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import com.zavedahmad.yaHabit.roomDatabase.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@HiltViewModel(assistedFactory = HabitDetailsPageViewModel.Factory::class)
class HabitDetailsPageViewModel @AssistedInject constructor(
    @Assisted val navKey: Screen.HabitDetailsPageRoute,
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao,
    val habitRepository: HabitRepository, val preferencesRepository: PreferencesRepository

) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Screen.HabitDetailsPageRoute): HabitDetailsPageViewModel
    }

    private val _habitAllData = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitAllData = _habitAllData.asStateFlow()
    private val _habitDetails = MutableStateFlow<HabitEntity?>(null)
    val habitDetails = _habitDetails.asStateFlow()
    private val _habitsPastYear = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitsPastYear: StateFlow<List<HabitCompletionEntity>?> = _habitsPastYear
    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()
    private val _firstDayOfWeek = MutableStateFlow<DayOfWeek?>(null)
    val firstDayOfWeek = _firstDayOfWeek.asStateFlow()
    private val _amoledTheme = MutableStateFlow<PreferenceEntity?>(null)
    val amoledTheme = _amoledTheme.asStateFlow()

    init {
        collectThemeMode()
        collectAmoledTheme()
        collectFirstDayOfWeek()
        getHabitDetails()

        getHabitAllData()
    }

    fun getHabitAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitCompletionsByIdFlow(navKey.habitId)
                .collect { _habitAllData.value = it }
        }
    }

    fun setFirstWeekOfDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            preferencesRepository.setFirstDayOfWeek(dayOfWeek)
        }
    }

    fun collectFirstDayOfWeek() {
        viewModelScope.launch {
            preferencesRepository.getFirstDayOfWeekFlow().collect { _firstDayOfWeek.value = it }
        }

    }

    fun getLastYearData() {
        val dateNow = LocalDate.now()
        val dateAYearAgo = dateNow.minusYears(1).toEpochDay()
        viewModelScope.launch(Dispatchers.IO) {

            habitCompletionDao.getEntriesAfterDate(navKey.habitId, dateAYearAgo)
                .collect { _habitsPastYear.value = it }
        }
    }


    fun deleteHabitById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { habitRepository.deleteHabit(id) }
    }

    fun getHabitDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.getHabitByIdFlow(navKey.habitId).collect { _habitDetails.value = it }
        }
    }

    fun collectThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("ThemeMode").collect { preference ->
                _themeMode.value = preference ?: PreferenceEntity("ThemeMode", "system")
            }
        }
    }

    fun collectAmoledTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("AmoledTheme").collect { preference ->
                _amoledTheme.value = preference ?: PreferenceEntity("AmoledTheme", "false")
            }
        }
    }


}