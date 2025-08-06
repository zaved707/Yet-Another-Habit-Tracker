package com.zavedahmad.yaHabit.ui.aboutPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.database.PreferenceEntity

import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AboutPageViewModel @Inject constructor(
    val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _allPreferences = MutableStateFlow<List<PreferenceEntity>>(emptyList())
    val allPreferences = _allPreferences.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.preferences.collect { _allPreferences.value = it }
        }
    }
}