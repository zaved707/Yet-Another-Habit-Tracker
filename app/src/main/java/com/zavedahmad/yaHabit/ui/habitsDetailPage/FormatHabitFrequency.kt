package com.zavedahmad.yaHabit.ui.habitsDetailPage

import com.zavedahmad.yaHabit.utils.formatNumberToReadable

fun formatHabitFrequency(streakType: String, frequency: Double, cycle: Int, formatFrequencyNumber : Boolean = false): String {
    val formattedNumber =  if (formatFrequencyNumber){
  formatNumberToReadable(frequency)}else{frequency.toString()}

    return when (streakType) {
        "everyday" -> {
            "$formattedNumber times EveryDay"
        }
        "week" -> {
            "$formattedNumber times per Week"
        }
        "month" -> {
            "$formattedNumber times per Month"
        }
        else -> {
            "$formattedNumber times per $cycle Days"
        }
    }
}