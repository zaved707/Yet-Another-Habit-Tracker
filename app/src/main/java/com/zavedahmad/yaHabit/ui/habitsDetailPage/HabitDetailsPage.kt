package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitDetailsPage(viewModel: HabitDetailsPageViewModel) {
    Scaffold { innerPadding ->
        val habitsPastYear = viewModel.habitsPastYear.collectAsStateWithLifecycle().value
        val today = LocalDate.now() // June 27, 2025
        val startDay = today.minusYears(1) // June 27, 2024
        val allDays = mutableListOf<LocalDate>()
        var currentDay = startDay
        while (currentDay <= today) {
            allDays.add(currentDay)
            currentDay = currentDay.plusDays(1)
        }
        val daysByMonth = mutableMapOf<String, MutableList<LocalDate>>()
        for (day in allDays) {
            val key = "${day.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${day.year}"
            if (!daysByMonth.containsKey(key)) {
                daysByMonth[key] = mutableListOf()
            }
            daysByMonth[key]?.add(day)
        }
        Column(Modifier.padding(innerPadding)) {
            if (habitsPastYear == null) {
                Text("Loading")
            } else {

                LazyHorizontalGrid(modifier = Modifier.height(100.dp),
                    rows = GridCells.Fixed(7),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp), // Gap between squares
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    daysByMonth.entries.sortedBy { it.value.first().toEpochDay() }.forEach { (monthYear, monthDays) ->

                        // Show month and year as a header
                        item(span = { GridItemSpan(7) }) { // Span all 7 columns
                         
                            Text(
                                text = monthYear, // e.g., "June 2024"
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }

                        // Show a square for each day
                        items(monthDays) { date ->
                            // Check if this day has a completion
                           val isCompleted= false
                           Column (
                                modifier = Modifier
                                    .size(10.dp) // Small square (24x24 pixels)
                                    .background(
                                        if (isCompleted) Color.Green else Color.Gray
                                    ),

                            ) {
                                // Show the day number in the square

                            }
                        }

                        // Add a gap after each month
                        item(span = { GridItemSpan(7) }) {
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }

            }
        }
    }
}