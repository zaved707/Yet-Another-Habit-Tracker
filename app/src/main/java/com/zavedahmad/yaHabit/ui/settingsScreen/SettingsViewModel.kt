package com.zavedahmad.yaHabit.ui.settingsScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.ImportExportRepository
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import com.zavedahmad.yaHabit.roomDatabase.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel()
class SettingsViewModel @Inject constructor(
    val preferencesDao: PreferencesDao,
    val preferencesRepository: PreferencesRepository,
    val importExportRepository: ImportExportRepository
) : ViewModel() {


    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()

    private val _dynamicColor = MutableStateFlow<PreferenceEntity?>(null)
    val dynamicColor = _dynamicColor.asStateFlow()

    private val _amoledTheme = MutableStateFlow<PreferenceEntity?>(null)
    val amoledTheme = _amoledTheme.asStateFlow()

    private val _firstDayOfWeek = MutableStateFlow<DayOfWeek?>(null)
    val firstDayOfWeek = _firstDayOfWeek.asStateFlow()

    init {

        collectThemeMode()
        collectDynamicColor()
        collectAmoledTheme()
        collectFirstDayOfWeek()
    }
    fun exportDatabase(context: Context, uri: Uri, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            importExportRepository.exportDatabase(uri =  uri , onComplete = onComplete)
        }
    }

    fun importDatabase(context: Context, uri: Uri, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            importExportRepository.importDatabase(uri = uri , onComplete = onComplete)
        }
    }

    fun setFirstWeekOfDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            preferencesRepository.setFirstDayOfWeek(dayOfWeek)
        }
    }
    fun collectFirstDayOfWeek(){
        viewModelScope.launch { preferencesRepository.getFirstDayOfWeekFlow().collect { _firstDayOfWeek.value = it } }

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