package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateItem(
    viewModel: MainPageViewModel,
    isCompleted: Boolean,
    date: LocalDate,
    habit: HabitEntity,
    columnWidth: Dp
) {
    Column(Modifier.width(columnWidth), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            date.dayOfWeek.toString().take(3),
            style = TextStyle(

                fontSize = 10.sp
            )
        )
        if (!isCompleted) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            viewModel.addHabitEntry(
                                habit.id,
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

        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            viewModel.deleteEntryByDateAndHabitId(
                                habit.id,
                                date
                            )
                        }
                        , hapticFeedbackEnabled = true
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
        }

    }
}