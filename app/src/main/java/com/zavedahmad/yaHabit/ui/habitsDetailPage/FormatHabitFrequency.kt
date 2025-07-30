package com.zavedahmad.yaHabit.ui.habitsDetailPage

import com.zavedahmad.yaHabit.roomDatabase.HabitStreakType
import com.zavedahmad.yaHabit.utils.formatNumber.formatNumberToReadable

fun formatHabitFrequency(streakType: HabitStreakType, frequency: Double, cycle: Int, formatFrequencyNumber : Boolean = false): String {
    val formattedNumber =  if (formatFrequencyNumber){
  formatNumberToReadable(frequency)}else{frequency.toString()}

    return when (streakType) {
        HabitStreakType.DAILY  -> {
            "$formattedNumber times EveryDay"
        }
        HabitStreakType.WEEKLY ->  {
            "$formattedNumber times per Week"
        }

        HabitStreakType.MONTHLY ->  {
            "$formattedNumber times per Month"
        }
        else -> {
            "$formattedNumber times per $cycle Days"
        }
    }
}