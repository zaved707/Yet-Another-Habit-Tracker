package com.zavedahmad.yaHabit.database.typeConverters

import androidx.room.TypeConverter
import com.zavedahmad.yaHabit.database.enums.HabitStreakType

class StreakTypeConverter {
    @TypeConverter
    fun toStreakType(streakType : String): HabitStreakType{
        return HabitStreakType.entries.find { it.dbValue == streakType } ?: HabitStreakType.DAILY
    }

    @TypeConverter
    fun fromStreakType(streakType: HabitStreakType): String{
        return streakType.dbValue
    }
}