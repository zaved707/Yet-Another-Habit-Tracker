package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StreakChartWidget(habitAllData: List<HabitCompletionEntity>?) {
    if (habitAllData != null) {
        val sortedData = habitAllData.sortedBy { it.completionDate }
        val streaks = mutableListOf<Triple<LocalDate, LocalDate, Int>>()
        var i = 0

        while (i < sortedData.size - 1) {
            val startDate = sortedData[i].completionDate
            var streak = 1

            while (i < sortedData.size - 1 && sortedData[i].completionDate.plusDays(1) == sortedData[i + 1].completionDate) {
                i += 1
                streak += 1
            }

            val endDate = sortedData[i].completionDate
            if (streak > 1) {
                streaks.add(Triple(startDate, endDate, streak))
            } else {
                i += 1
            }


        }
        streaks.sortByDescending { it.third }
        val topThreeStreaks = streaks.take(5).toMutableList()
//        topThreeStreaks.sortByDescending { it.first }
        ShowStreaksBars(topThreeStreaks)

    }
}

@Composable
private fun ShowStreaksBars(streaks: List<Triple<LocalDate, LocalDate, Int>>) {
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yy")
    val textStyle = TextStyle(color = MaterialTheme.colorScheme.primary.copy(0.8f))
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        streaks.forEach { it ->
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(it.first.format(dateFormatter), style = textStyle)
                Text(" - ")
                Text(it.second.format(dateFormatter), style = textStyle)

                Text(": " + it.third.toString())
            }
            Column(

                Modifier
                    .fillMaxWidth((it.third.toFloat() / streaks[0].third.toFloat()))
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {}
            }
        }
    }
}