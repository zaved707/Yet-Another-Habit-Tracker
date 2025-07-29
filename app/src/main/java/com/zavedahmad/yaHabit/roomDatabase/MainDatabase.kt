package com.zavedahmad.yaHabit.roomDatabase


import androidx.room.AutoMigration
import androidx.room.Database

import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zavedahmad.yaHabit.roomDatabase.typeConverters.ColorConvertor
import com.zavedahmad.yaHabit.roomDatabase.typeConverters.LocalDateConverter
import com.zavedahmad.yaHabit.roomDatabase.typeConverters.StreakTypeConverter


@Database(
    entities = [PreferenceEntity::class, HabitEntity::class, HabitCompletionEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],

    )

@TypeConverters(LocalDateConverter::class, ColorConvertor::class, StreakTypeConverter::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun preferencesDao(): PreferencesDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao

}