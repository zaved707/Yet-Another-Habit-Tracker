package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import java.time.LocalDate

@Composable
fun StreakChartWidget(habitAllData: List<HabitCompletionEntity>?) {
    if (habitAllData != null) {
        val sortedData = habitAllData.sortedBy { it.completionDate }
        val streaks = mutableListOf<Triple<LocalDate, LocalDate, Int>>()
        var i = 0

        while (i < sortedData.size - 1) {
            val startDate = sortedData[i].completionDate
           var streak = 1

            while(i < sortedData.size- 1 && sortedData[i].completionDate.plusDays(1) == sortedData[i + 1].completionDate) {
                i+= 1
                streak+=1
            }

            val endDate =  sortedData[i].completionDate
            if (streak > 1) {
                streaks.add(Triple(startDate, endDate, streak))
            }else{i+=1}


        }
        streaks
        Text(streaks.toString())
    }
}