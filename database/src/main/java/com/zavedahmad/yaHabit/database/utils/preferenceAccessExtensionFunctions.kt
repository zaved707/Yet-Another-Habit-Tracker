package com.zavedahmad.yaHabit.database.utils

import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository


fun List<PreferenceEntity>.getTheme(): String {
    return find { it.accessKey == "theme" }?.value ?: PreferencesRepository.defaultPreferences["ThemeMode"] ?: ""
}

fun List<PreferenceEntity>.getAmoledThemeMode(): String {
    return find { it.accessKey == "theme" }?.value ?: PreferencesRepository.defaultPreferences["AmoledTheme"] ?: ""
}
