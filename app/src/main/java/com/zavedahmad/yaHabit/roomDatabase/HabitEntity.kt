package com.zavedahmad.yaHabit.roomDatabase

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HabitTable")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val index: Int = 0,
    val name: String,
    val description : String= "",
    val color : Color,
    val streakType : String,
    val frequency : Int,
    val cycle : Int,
    val completionsRequiredPerDay : Float =1f,
    val measurementUnit : String
)
