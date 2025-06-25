package com.zavedahmad.yaHabit.roomDatabase


import androidx.room.Database

import androidx.room.RoomDatabase


@Database(entities = [ PreferenceEntity::class], version = 1, exportSchema = false)

abstract class MainDatabase : RoomDatabase(){
    abstract fun preferencesDao(): PreferencesDao

}