package com.zavedahmad.yaHabit.roomDatabase

import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    val preferencesDao: PreferencesDao,
    val db: MainDatabase
) {

}