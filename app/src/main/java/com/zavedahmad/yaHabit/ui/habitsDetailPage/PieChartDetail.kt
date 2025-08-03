package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.isAbsolute
import com.zavedahmad.yaHabit.roomDatabase.isPartial
import com.zavedahmad.yaHabit.roomDatabase.isSkip
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun PieChartDetail(habitAllData : List<HabitCompletionEntity>?) {
    val numberOfPartials by remember(habitAllData) {  derivedStateOf{( habitAllData?.filter { it.isPartial() && !it.isSkip() }?.size ?: 0)}}
    val numberOfAbsolute by remember(habitAllData) { derivedStateOf {  ( habitAllData?.filter { it.isAbsolute() }?.size ?: 0)}}
    val numberOfSkips by remember(habitAllData ){derivedStateOf { habitAllData?.filter { it.isSkip() }?.size ?: 0 }}
    val color1 = MaterialTheme.colorScheme.primary.copy(0.5f)
    val color2 = MaterialTheme.colorScheme.primary
    val color3  = MaterialTheme.colorScheme.tertiary
    val data = remember(habitAllData) {
        mutableStateOf(
            listOf(
                Pie(label = "Partial", data = numberOfPartials.toDouble(), color = color1 , selectedColor = Color.Green),
                Pie(label = "Absolute", data = numberOfAbsolute.toDouble(), color =color2, selectedColor = Color.Blue),
                Pie(label = "Skipped", data = numberOfSkips.toDouble(), color =color3, selectedColor = Color.Blue),

            )
        )
    }
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
        Column {
           Text("Absolute: $numberOfAbsolute" , color = color2)

            Text("Partial: $numberOfPartials", color = color1)
            Text("Skipped: $numberOfSkips", color = color3)


        }
        Surface (Modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)){
            Box(Modifier.padding(8.dp)){
    PieChart(
        modifier = Modifier.size(200.dp),
        data = data.value,
        style = Pie.Style.Stroke( 30.dp)
    )}}}
}
