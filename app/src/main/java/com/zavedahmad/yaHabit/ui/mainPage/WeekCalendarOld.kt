package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity

@Composable
fun WeekCalendarOld(
    viewModel: MainPageViewModel,
    habit: HabitEntity
) {
    val completions = viewModel.getHabitCompletionsByHabitId(habit.id)
        .collectAsStateWithLifecycle(initialValue = emptyList()).value
    var dates by rememberSaveable { mutableStateOf(viewModel.generateInitialDates()) }
    LazyRow(
        reverseLayout = true,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        items(dates) { date ->
            val isCompleted = if (completions == null) {
                false
            } else {
                completions.any { it.completionDate == date }
            }

            DateItem(viewModel, isCompleted, date, habit, 50.dp)
        }
    }
}