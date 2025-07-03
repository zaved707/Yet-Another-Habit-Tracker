package com.zavedahmad.yaHabit.roomDatabase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "habitCompletion",
    foreignKeys = [ForeignKey(
        entity = HabitEntity::class,
        parentColumns = ["id"],
        childColumns = ["habitId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HabitCompletionEntity(
    val habitId: Int,
    val completionDate: LocalDate,
    val partial : Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
