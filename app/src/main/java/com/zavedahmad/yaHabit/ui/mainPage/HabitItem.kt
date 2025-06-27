package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import kotlin.collections.plus

@Composable
fun HabitItem(viewModel: MainPageViewModel, habit: HabitEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        habit.name,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        habit.description,

                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Icon(Icons.Default.Start, contentDescription = "")
            }
            Spacer(Modifier.height(20.dp))


            val completions = viewModel.getHabitCompletionsByHabitId(habit.id)
                .collectAsStateWithLifecycle(initialValue = emptyList())
            var dates by rememberSaveable { mutableStateOf(viewModel.generateInitialDates()) }
            LazyRow(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                items(dates) { date ->
                    val isCompleted =
                        completions.value.any { it.completionDate == date }


                    DateItem(viewModel, isCompleted, date, habit, 50.dp)
                }
            }

        }

        HorizontalDivider()

        Column(
            Modifier
                .fillMaxWidth()
                ,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column { }
                IconButton(modifier = Modifier, onClick = {viewModel.deleteHabitById( habit.id)}) {
                Icon(Icons.Default.Delete, contentDescription = "")
            }}
        }
    }
}