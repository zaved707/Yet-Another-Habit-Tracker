package com.zavedahmad.yaHabit.database.typeConverters

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.fromColorLong
import androidx.compose.ui.graphics.toColorLong
import androidx.room.TypeConverter

class ColorConvertor {
    @TypeConverter
    fun fromColor(color: Color ) : Long{
        return color.toColorLong()
    }

    @TypeConverter
    fun toColor(colorLong: Long): Color{
        return Color.fromColorLong(colorLong)
    }
}