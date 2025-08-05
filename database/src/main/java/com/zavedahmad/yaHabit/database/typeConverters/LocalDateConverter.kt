package com.zavedahmad.yaHabit.database.typeConverters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }
    @TypeConverter
    fun toLocalDate(date : Long): LocalDate{
        return LocalDate.ofEpochDay(date)
    }


}