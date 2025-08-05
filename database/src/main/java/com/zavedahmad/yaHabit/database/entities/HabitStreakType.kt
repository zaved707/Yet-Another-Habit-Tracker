package com.zavedahmad.yaHabit.database.entities



enum class HabitStreakType(val dbValue: String) {
    DAILY("everyday"),
    MONTHLY("month"),
    WEEKLY("week"),
    CUSTOM("custom")    //TODO  everyday, week, month, custom respectively in database now write migrations
}