package com.zavedahmad.yaHabit.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zavedahmad.yaHabit.database.PreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {
    @Upsert
    suspend fun updatePreference(preferenceEntity: PreferenceEntity)


    @Query("SELECT * FROM PreferencesTable WHERE accessKey = :key ")
    fun getPreferenceFlow(key: String): Flow<PreferenceEntity?>

    @Query("SELECT * FROM PreferencesTable")
    fun getAllPreferencesFlow() :  Flow<List<PreferenceEntity>>

    @Query("SELECT * FROM PreferencesTable WHERE accessKey = :key ")
    suspend fun getPreference(key: String): PreferenceEntity?
}