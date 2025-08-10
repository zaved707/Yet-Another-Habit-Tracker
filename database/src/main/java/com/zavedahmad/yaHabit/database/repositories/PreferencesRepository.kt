package com.zavedahmad.yaHabit.database.repositories

import com.zavedahmad.yaHabit.database.MainDatabase
import com.zavedahmad.yaHabit.database.PreferenceEntity
import com.zavedahmad.yaHabit.database.constants.PreferenceKeys
import com.zavedahmad.yaHabit.database.daos.PreferencesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import javax.inject.Inject


class PreferencesRepository @Inject constructor(
    val preferencesDao: PreferencesDao,
    val db: MainDatabase
) {
    companion object {
        val defaultPreferences = mapOf(
            PreferenceKeys.AmoledTheme.key to PreferenceKeys.AmoledTheme.defaultValue,
            PreferenceKeys.ThemeMode.key to PreferenceKeys.ThemeMode.defaultValue,
            PreferenceKeys.FirstDayOfWeek.key to PreferenceKeys.FirstDayOfWeek.defaultValue,
            PreferenceKeys.ShowArchive.key to PreferenceKeys.ShowArchive.defaultValue,
            PreferenceKeys.ShowActive.key to PreferenceKeys.ShowActive.defaultValue
        )
    }


    val preferences: Flow<List<PreferenceEntity>> =
        preferencesDao.getAllPreferencesFlow().map { dbPrefs ->

            val dbPrefMap = dbPrefs.associateBy { it.accessKey }

            defaultPreferences.map { (key, defaultValue) ->
                dbPrefMap[key] ?: PreferenceEntity(key, defaultValue)
            }


        }.distinctUntilChanged()

    suspend fun setAmoledTheme(amoledThemeValue: String) {
        preferencesDao.updatePreference(
            preferenceEntity = PreferenceEntity(
                accessKey = "AmoledTheme",
                value = amoledThemeValue
            )
        )

    }

    suspend fun setPreference(key:String, value: String){
        preferencesDao.updatePreference(
            preferenceEntity = PreferenceEntity(
                accessKey = key,
                value = value
            )
        )
    }



    suspend fun setFirstDayOfWeek(dayOfWeek: DayOfWeek) {
        preferencesDao.updatePreference(
            preferenceEntity = PreferenceEntity(
                accessKey = "firstDayOfWeek",
                value = dayOfWeek.value.toString()
            )
        )

    }

    fun getFirstDayOfWeekFlow(): Flow<DayOfWeek> {
        return preferencesDao.getPreferenceFlow("firstDayOfWeek").map { preference ->
            if (preference != null) {
                DayOfWeek.of(preference.value.toInt())
            } else {
                DayOfWeek.SUNDAY
            }

        }
    }

}