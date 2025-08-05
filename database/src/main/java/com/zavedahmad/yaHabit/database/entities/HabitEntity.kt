package com.zavedahmad.yaHabit.database.entities

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zavedahmad.yaHabit.database.enums.HabitStreakType

@Entity(tableName = "HabitTable")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val index: Int = 0,
    val name: String,
    val description : String= "",
    val color : Color,
    val streakType : HabitStreakType,
    val frequency :Double,
    val cycle : Int,
    @ColumnInfo(defaultValue = "1.0")
    val repetitionPerDay : Double,
    @ColumnInfo(defaultValue = "Unit")
    val measurementUnit : String,

    )