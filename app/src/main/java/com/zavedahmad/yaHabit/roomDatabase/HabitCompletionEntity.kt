package com.zavedahmad.yaHabit.roomDatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
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
    val partial: Boolean = false,
    @ColumnInfo(defaultValue = "1.0")
    val repetitionsOnThisDay: Double = 0.0,

    val note: String? = null,
    @ColumnInfo(defaultValue = "false")
    val skip: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

) : Parcelable

fun HabitCompletionEntity.isPartial(): Boolean {
    return this.partial
}
fun HabitCompletionEntity.onlyPartial():Boolean{
    return this.partial
}
fun HabitCompletionEntity.isAbsolute(): Boolean {
    return (this.repetitionsOnThisDay > 0.0)
}

fun HabitCompletionEntity.isDeletable(): Boolean {
    return (this.note == null && !this.isPartial() && !this.isAbsolute() && !this.isSkip())
}

fun HabitCompletionEntity.isOnlyNote(): Boolean {
    return (!this.isPartial() && !this.isAbsolute() && this.note != null)
}

fun HabitCompletionEntity.hasNote(): Boolean {
    return this.note != null
}

fun HabitCompletionEntity.isSkip(): Boolean {
    return this.skip
}


fun HabitCompletionEntity.state(): String {
    return if (this.isOnlyNote()) {
        "empty"
    } else {
        if (this.isSkip()) {
            "skip"
        } else {
            if (this.isPartial()) {
                "partial"
            } else {
                if (this.repetitionsOnThisDay == 0.0) {
                    "empty"  //deletable
                } else {

                    "absolute"
                }
            }
        }
    }
}