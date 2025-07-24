package com.zavedahmad.yaHabit.roomDatabase


import androidx.room.Database

import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.zavedahmad.yaHabit.roomDatabase.typeConverters.ColorConvertor
import com.zavedahmad.yaHabit.roomDatabase.typeConverters.LocalDateConverter


@Database(entities = [ PreferenceEntity::class, HabitEntity::class, HabitCompletionEntity::class], version = 1, exportSchema = true)
@TypeConverters(LocalDateConverter::class, ColorConvertor::class)
abstract class MainDatabase : RoomDatabase(){
    abstract fun preferencesDao(): PreferencesDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao

}