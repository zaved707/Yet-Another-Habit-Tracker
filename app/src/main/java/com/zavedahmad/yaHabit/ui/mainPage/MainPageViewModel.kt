package com.zavedahmad.yaHabit.ui.mainPage



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.constants.PreferenceKeys
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainPageViewModel(
    val habitRepository: HabitRepository,
    val preferencesRepository: PreferencesRepository,


    ) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()
    private val _allPreferences = MutableStateFlow<List<PreferenceEntity>>(emptyList())
    val allPreferences = _allPreferences.asStateFlow()
    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits
    private val _isReorderableMode = MutableStateFlow(false)
    val isReorderableMode = _isReorderableMode.asStateFlow()

    private val showArchive = MutableStateFlow(false)

    private val _devMode = MutableStateFlow<Boolean>(false)
    val devMode = _devMode.asStateFlow()

    init {
        collectPreferences()
        collectHabits()

    }

    fun collectHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabitsFlowSortedByIndex().collect { it ->
                _habits.value = it
            }
        }
    }

    fun setArchivedFilter(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.setPreference(PreferenceKeys.ShowArchive.key, value.toString())
        }
    }

    fun setActiveFilter(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.setPreference(PreferenceKeys.ShowActive.key, value.toString())
        }
    }

    fun collectPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.preferences.collect { _allPreferences.value = it }
        }
    }

    fun setBottomSheetVisibility(value: Boolean) {
        _isBottomSheetVisible.value = value
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

    fun archive(id: Int): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.archive(id)
        }

    }

    fun unArchive(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.unArchive(id)
        }
    }


}

