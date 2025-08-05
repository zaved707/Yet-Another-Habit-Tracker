package com.zavedahmad.yaHabit.roomDatabase

import com.zavedahmad.yaHabit.database.PreferenceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    val preferencesDao: PreferencesDao,
    val db: MainDatabase
) {
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