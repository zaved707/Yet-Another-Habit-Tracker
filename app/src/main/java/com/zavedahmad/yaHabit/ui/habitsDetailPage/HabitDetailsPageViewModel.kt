package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.daos.HabitCompletionDao
import com.zavedahmad.yaHabit.database.daos.HabitDao
import com.zavedahmad.yaHabit.database.daos.PreferencesDao
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HabitDetailsPageViewModel(
   val navKey: Screen.HabitDetailsPageRoute,
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao,
    val habitRepository: HabitRepository,
    val preferencesRepository: PreferencesRepository

) : ViewModel() {



    private val _allPreferences = MutableStateFlow<List<PreferenceEntity>>(emptyList())
    val allPreferences = _allPreferences.asStateFlow()
    private val _habitAllData = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitAllData = _habitAllData.asStateFlow()
    private val _habitDetails = MutableStateFlow<HabitEntity?>(null)
    val habitDetails = _habitDetails.asStateFlow()
    private val _habitsPastYear = MutableStateFlow<List<HabitCompletionEntity>?>(null)
    val habitsPastYear: StateFlow<List<HabitCompletionEntity>?> = _habitsPastYear



    init {

        getHabitDetails()
        collectPreferences()
        getHabitAllData()
    }

    fun getHabitAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitCompletionsByIdFlow(navKey.habitId)
                .collect { _habitAllData.value = it }
        }
    }






    fun collectPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.preferences.collect { _allPreferences.value = it }
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