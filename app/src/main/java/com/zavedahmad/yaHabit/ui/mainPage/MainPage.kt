package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.Screen
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    val habits = viewModel.habits.collectAsStateWithLifecycle()
    val listStartDate = viewModel.listStartDate.collectAsStateWithLifecycle()
    val listEndDate = viewModel.listEndDate.collectAsStateWithLifecycle()
    var dates by rememberSaveable { mutableStateOf(generateInitialDates()) }
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(onClick = { backStack.add(Screen.AddHabitPageRoute) }) { }
            Row(modifier = Modifier.fillMaxHeight()) {
                Text(dates.size.toString())
                LazyColumn(modifier = Modifier.fillMaxWidth(0.5f)) {
                    items(habits.value) { habit ->
                        Text(habit.name)
                        var today =LocalDate.now().toEpochDay()
                        val string = today.toString()



                        if (string == today.toString()){

                        }
                    }
                }
                VerticalDivider()
                LazyRow {
                    items(dates) { date ->
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                date.dayOfMonth.toString()
                            )
                            Text(date.month.toString())
                            val completions =  viewModel.getHabitCompletionsByDate(date.toEpochDay()).collectAsStateWithLifecycle(initialValue = emptyList())
                            habits.value.forEach { habit ->
                                val isCompleted = completions.value.any { it.habitId == habit.id }
                                if(!isCompleted){
                                    IconButton(onClick = {viewModel.addHabitEntry(habit.id, date)}) { Icon(Icons.Default.Close, contentDescription = "Mark as not completed") }
                                }
                                else{
                                   IconButton(onClick = {}) {  Icon(Icons.Default.Check, contentDescription = "")}
                                }
                            }
                        }
                        if (dates.indexOf(date) > dates.size - 5) {
                            dates = dates + generateMoreDates(dates.last())
                        }
//                        if (dates.indexOf(date) == 5) {
//                            dates = dates + generateMoreDatesInverse(dates.first())
//                            dates = dates.sortedByDescending{it}
//                        }
//                        if (dates.size > 60) {
//                            dates=  dates.takeLast(60).toMutableList()
//                        }
                    }
                }
            }
        }
    }
}

private fun generateInitialDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (0L..30L).map { today.minusDays(it) }
}
fun fetchDates(initialDate : LocalDate, lastDate: LocalDate): List<LocalDate>{
    return generateSequence(initialDate) { it.minusDays(1) }
        .takeWhile { !it.isBefore(lastDate) }
        .toList()
}
// Generate more dates starting from the last date
private fun generateMoreDates(lastDate: LocalDate): List<LocalDate> {
    return (1L..30L).map { lastDate.minusDays(it) }
}
private fun generateMoreDatesInverse(firstDate : LocalDate): List<LocalDate>{
    return (1L..30L).map {firstDate.plusDays(it)  }
}
