package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.yearMonth
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import java.time.Year
import java.time.YearMonth
import kotlin.collections.listOf

@Composable
fun ColumnChartWidget(habitAllData: List<HabitCompletionEntity>?) {
    val yearToShow = remember { mutableStateOf(Year.now()) }
    val currentYearData by remember(habitAllData) {
        derivedStateOf {
            habitAllData?.filter { Year.from(it.completionDate) == yearToShow.value } ?: emptyList()
        }
    }

    val allMonths by remember {
        derivedStateOf {
            (1..12).map { i ->
                YearMonth.of(
                    yearToShow.value.value,
                    i
                )
            }
        }
    }

    val barColor = MaterialTheme.colorScheme.primary

    val data by remember(habitAllData, yearToShow) {
        derivedStateOf {

                allMonths.map { month ->
                    Bars(
                        label = month.month.name.slice(0..2), values = listOf(
                            Bars.Data(
                                value = currentYearData?.filter { it.completionDate.yearMonth == month }?.size?.toDouble()
                                    ?: 0.0, color = SolidColor(barColor)
                            )
                        )
                    )
                }

        }


    }
    Column {
        ColumnChart(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            data = data,
            animationSpec = snap(),
            animationDelay = 0,
            animationMode = AnimationMode.Together(),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            ),
            labelProperties = LabelProperties(
                enabled = true,
                builder = { modifier, label, shouldRotate, index ->
                    Text(
                        label, style = TextStyle(fontSize = 10.sp)
                    )
                }

            ), labelHelperProperties = LabelHelperProperties(enabled = false)


        )


    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(onClick = {
            yearToShow.value = yearToShow.value.minusYears(1)
        }) {
            Icon(
                Icons.Default.ArrowBackIosNew,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            )
        }
        Text(
            text = yearToShow.value.toString(),
            Modifier.clickable(onClick = { yearToShow.value = Year.now() }),
        )
        Card(onClick = {
            yearToShow.value = yearToShow.value.plusYears(1)
        }) {
            Icon(
                Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            )
        }
    }
}