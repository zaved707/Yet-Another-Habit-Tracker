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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
                "PRIMARY KEY(`id`))")
    }
}

@Database(
    entities = [PreferenceEntity::class, HabitEntity::class, HabitCompletionEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],

)

@TypeConverters(LocalDateConverter::class, ColorConvertor::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun preferencesDao(): PreferencesDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao

}