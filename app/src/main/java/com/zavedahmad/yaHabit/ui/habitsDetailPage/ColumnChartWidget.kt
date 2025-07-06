package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.Bars
import kotlin.collections.listOf

@Composable
fun ColumnChartWidget(habitAllData: List<HabitCompletionEntity>?) {

    val data = remember(habitAllData) {
        listOf(
            Bars(
                label = "Jan", values = listOf(
                    Bars.Data(value = 100.0, color = SolidColor(Color.Green))
                )
            ),
            Bars(label = "feb", values = listOf(Bars.Data(value = 70.toDouble(), color = SolidColor(Color.Red)))

            )
        )
    }
    ColumnChart(modifier = Modifier.height(200.dp).fillMaxWidth(), data = data)

}