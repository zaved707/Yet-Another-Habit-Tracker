package com.zavedahmad.yaHabit.utils

import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import java.time.LocalDate

fun convertHabitCompletionEntityListToDatesList(habits :List<HabitCompletionEntity>): List<LocalDate>{
    val dates = mutableListOf<LocalDate>()
    for(i in habits){
        dates.add(i.completionDate)
    }
    return dates

}