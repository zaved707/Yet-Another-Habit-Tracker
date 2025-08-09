package com.zavedahmad.yaHabit.ui.mainPage


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.database.daos.HabitCompletionDao
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.daos.HabitDao
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.ImportExportRepository
import com.zavedahmad.yaHabit.database.MainDatabase
import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.daos.PreferencesDao
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import com.zavedahmad.yaHabit.database.utils.DatabaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate


@HiltViewModel()
class MainPageViewModel @Inject constructor(
    val habitRepository: HabitRepository,
    val preferencesRepository: PreferencesRepository,
    @ApplicationContext val context: Context,

) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }

    private val _allPreferences = MutableStateFlow<List<PreferenceEntity>>(emptyList())
    val allPreferences = _allPreferences.asStateFlow()
    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits
    private val _isReorderableMode = MutableStateFlow(false)
    val isReorderableMode = _isReorderableMode.asStateFlow()



    private val _devMode = MutableStateFlow<Boolean>(false)
    val devMode = _devMode.asStateFlow()

    init {
        collectPreferences()

        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabitsFlowSortedByIndex().collect { it -> _habits.value = it }
        }

    }

    fun collectPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.preferences.collect { _allPreferences.value = it }
        }
    }

    fun changeReorderableMode(value: Boolean) {
        _isReorderableMode.value = value
    }


    fun deleteAllHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.deleteAllHabits()
        }
    }


    fun addSampleHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.addSampleHabits()
        }
    }

    fun deleteHabitById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { habitRepository.deleteHabit(id) }
    }


    fun move(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.move(from, to)
        }
    }


}

