package com.zavedahmad.yaHabit.roomDatabase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
@Parcelize
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
    val repetitionsOnThisDay :Double = 1.0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

): Parcelable
