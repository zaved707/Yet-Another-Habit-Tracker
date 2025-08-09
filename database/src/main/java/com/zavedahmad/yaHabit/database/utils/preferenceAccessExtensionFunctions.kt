package com.zavedahmad.yaHabit.database.utils

import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import java.time.DayOfWeek


fun List<PreferenceEntity>.getTheme(): String {
    return find { it.accessKey == "ThemeMode" }?.value ?: PreferencesRepository.defaultPreferences["ThemeMode"] ?: ""
}

fun List<PreferenceEntity>.getAmoledThemeMode(): Boolean {

    return (find { it.accessKey == "AmoledTheme" }?.value ?: PreferencesRepository.defaultPreferences["AmoledTheme"] ?: "").toBoolean()
}

fun List<PreferenceEntity>.getFirstDayOfWeek(): DayOfWeek{
    return DayOfWeek.of((find{it.accessKey == "firstDayOfWeek"}?. value ?: PreferencesRepository.defaultPreferences["firstDayOfWeek"] ?: "").toInt())
}
