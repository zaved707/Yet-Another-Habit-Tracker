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
    val partial: Boolean = false,
    val repetitionsOnThisDay: Double = 0.0,
    val note: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

) : Parcelable

fun HabitCompletionEntity.isPartial(): Boolean {
    return this.partial
}

fun HabitCompletionEntity.isAbsolute(): Boolean {
    return (this.repetitionsOnThisDay > 0.0)
}

fun HabitCompletionEntity.isDeletable(): Boolean {
    return (this.note == null && !this.isPartial() && !this.isAbsolute())
}

fun HabitCompletionEntity.isOnlyNote(): Boolean {
    return (!this.isPartial() && !this.isAbsolute() && this.note != null)
}
fun HabitCompletionEntity.hasNote(): Boolean{
    return this.note!=null
}
fun HabitCompletionEntity.copy(
    habitId: Int?,
    completionDate: LocalDate?,
    partial: Boolean?,
    repetitionsOnThisDay: Double?,
    note: String?,
    id: Int?
): HabitCompletionEntity {
    return HabitCompletionEntity(
        habitId = habitId ?: this.habitId,
        completionDate = completionDate ?: this.completionDate,
        partial = partial ?: this.partial,
        repetitionsOnThisDay = repetitionsOnThisDay ?: this.repetitionsOnThisDay,
        note = note ?: this.note,
        id = id ?: this.id
    )
}

fun HabitCompletionEntity.state(): String {
    return if (this.isOnlyNote()) {
        "note"
    } else {
        if (this.isPartial()){
            "partial"
        }else{
            "absolute"
        }

    }
}