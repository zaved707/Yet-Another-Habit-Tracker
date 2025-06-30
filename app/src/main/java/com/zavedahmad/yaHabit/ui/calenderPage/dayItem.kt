package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import java.time.LocalDate

@Composable
fun DayItem(day: CalendarDay, state: String, viewModel: CalenderPageViewModel) {
    val date = day.date

    var bgColor = MaterialTheme.colorScheme.surfaceVariant
    var textColor = MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
    if (state == "partial") {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        viewModel.addHabitEntry(
                            viewModel.navKey.habitId,
                            date
                        )
                    },
                    hapticFeedbackEnabled = true
                )
                .size(35.dp),
            border = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
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


    } else if (state == "absolute") {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        viewModel.deleteEntryByDateAndHabitId(
                            viewModel.navKey.habitId,
                            date.toEpochDay()
                        )
                    }, hapticFeedbackEnabled = true
                )
                .size(35.dp)

        ) {
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Icon(Icons.Default.Check, contentDescription = "") }
        }
    }else{
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        viewModel.addHabitEntry(
                            viewModel.navKey.habitId,
                            date
                        )
                    },
                    hapticFeedbackEnabled = true
                )
                .size(35.dp),
            border = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
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
    }

}

}

