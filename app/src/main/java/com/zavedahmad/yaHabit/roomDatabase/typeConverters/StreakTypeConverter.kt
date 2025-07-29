package com.zavedahmad.yaHabit.roomDatabase.typeConverters

import androidx.room.TypeConverter
import com.zavedahmad.yaHabit.roomDatabase.HabitStreakType

class StreakTypeConverter {
    @TypeConverter
    fun toStreakType(streakType : String): HabitStreakType{
        return HabitStreakType.valueOf(streakType)
    }

    @TypeConverter
    fun fromStreakType(streakType: HabitStreakType): String{
        return streakType.name
    }
}