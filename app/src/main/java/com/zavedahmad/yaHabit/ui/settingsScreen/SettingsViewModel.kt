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


    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()

    private val _dynamicColor = MutableStateFlow<PreferenceEntity?>(null)
    val dynamicColor = _dynamicColor.asStateFlow()

    private val _amoledTheme = MutableStateFlow<PreferenceEntity?>(null)
    val amoledTheme = _amoledTheme.asStateFlow()


    init {

        collectThemeMode()
        collectDynamicColor()
        collectAmoledTheme()
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

    fun setDynamicColor(value: String) {
        viewModelScope.launch {
            preferencesDao.updatePreference(PreferenceEntity("DynamicColor", value))
        }
    }

    fun collectDynamicColor() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("DynamicColor").collect { preference ->
                _dynamicColor.value = preference ?: PreferenceEntity("DynamicColor", "false")
            }
        }
    }

    fun setAmoledTheme(value: String) {
        viewModelScope.launch {
            preferencesDao.updatePreference(PreferenceEntity("AmoledTheme", value))
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