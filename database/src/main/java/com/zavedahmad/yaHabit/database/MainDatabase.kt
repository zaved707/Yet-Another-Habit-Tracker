package com.zavedahmad.yaHabit.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zavedahmad.yaHabit.database.daos.HabitCompletionDao
import com.zavedahmad.yaHabit.database.daos.HabitDao
import com.zavedahmad.yaHabit.database.daos.PreferencesDao
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.typeConverters.ColorConvertor
import com.zavedahmad.yaHabit.database.typeConverters.LocalDateConverter
import com.zavedahmad.yaHabit.database.typeConverters.StreakTypeConverter

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