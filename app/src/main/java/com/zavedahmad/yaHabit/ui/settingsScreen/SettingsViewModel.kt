package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class SettingsViewModel @Inject constructor(val preferencesDao: PreferencesDao) : ViewModel() {
    private val _userInput = MutableStateFlow("")


    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()

    init {

        collectThemeMode()
    }

    fun setTheme(value: String) {
        viewModelScope.launch {
            preferencesDao.updatePreference(PreferenceEntity("ThemeMode", value))
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