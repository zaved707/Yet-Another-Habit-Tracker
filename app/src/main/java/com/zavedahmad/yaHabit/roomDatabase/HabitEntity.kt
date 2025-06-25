package com.zavedahmad.yaHabit.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HabitTable")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String

)
