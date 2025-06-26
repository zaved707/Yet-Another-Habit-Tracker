package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import java.time.LocalDate

@Composable
fun DateItem(viewModel: MainPageViewModel, isCompleted: Boolean, date: LocalDate, habit: HabitEntity,columnWidth : Dp){
    Column(Modifier.width(columnWidth), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            date.dayOfWeek.toString().take(3),
            style = TextStyle(
                fontWeight = FontWeight.Thin,
                fontSize = 10.sp
            )
        )
        if (!isCompleted) {
            Card(
                onClick = {
                    (viewModel.addHabitEntry(
                        habit.id,
                        date
                    ))
                },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
                modifier = Modifier
                    .padding(5.dp)
                    .size(35.dp),
                border = BorderStroke(width = 5.dp, color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(date.dayOfMonth.toString())
                }
            }

        } else {
            Card(
                onClick = {
                    viewModel.deleteEntryByDateAndHabitId(
                        habit.id,
                        date.toEpochDay()
                    )
                },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(5.dp)
                    .size(35.dp),

            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(Icons.Default.Check, contentDescription = "") }
            }
        }

    }
}