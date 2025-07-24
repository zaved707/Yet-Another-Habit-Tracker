package com.zavedahmad.yaHabit.roomDatabase

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
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
    val frequency :Double,
    val cycle : Int,
    @ColumnInfo(defaultValue = "1.0")
    val repetitionPerDay : Double ,   //Todo add migration
    @ColumnInfo(defaultValue = "Unit")
    val measurementUnit : String ,   //Todo add migration

)
