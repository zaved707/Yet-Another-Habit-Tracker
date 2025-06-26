package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.components.MyTopABCommon
import java.time.LocalDate

import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    val habits = viewModel.habits.collectAsStateWithLifecycle()
    val sharedScrollState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = {
        1
    })
    var dates by rememberSaveable { mutableStateOf(generateInitialDates()) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var lazyRowWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { MyTopABCommon(backStack, scrollBehavior, "Habits") },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { backStack.add(Screen.AddHabitPageRoute) }) {
                Text("Add Habit")
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {


            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(habits.value) { habit ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                habit.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                                ,
                                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(Modifier.height(20.dp))
                            val week = LocalDate.now()

                            val completions = viewModel.getHabitCompletionsByHabitId(habit.id)
                                .collectAsStateWithLifecycle(initialValue = emptyList())
                            var dates by rememberSaveable { mutableStateOf(generateInitialDates()) }
                            LazyRow(
                                reverseLayout = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    ) {
                                val columnWidth = lazyRowWidth / 7
                                items(dates) { date ->
                                    val isCompleted =
                                        completions.value.any { it.completionDate == date }

                                    if (dates.indexOf(date) > dates.size - 5) {
                                        dates = dates + generateMoreDates(dates.last())
                                    }
                                    DateItem(viewModel, isCompleted, date, habit, 50.dp)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(40.dp))


                }
            }


            /*  LazyRow() {
                  items(dates) { date ->
                      Column(modifier = Modifier.padding(20.dp)) {
                          Text(
                              date.dayOfMonth.toString()
                          )
                          Text(date.month.toString())
                          val completions = viewModel.getHabitCompletionsByDate(date.toEpochDay())
                              .collectAsStateWithLifecycle(initialValue = emptyList())
                          LazyColumn(state = sharedScrollState) {
                              items(habits.value) { habit ->
                                  val isCompleted =
                                      completions.value.any { it.habitId == habit.id }
                                  if (!isCompleted) {
                                      IconButton(onClick = {
                                          viewModel.addHabitEntry(
                                              habit.id,
                                              date
                                          )
                                      }) {
                                          Icon(
                                              Icons.Default.Close,
                                              contentDescription = "Mark as not completed"
                                          )
                                      }
                                  } else {
                                      IconButton(onClick = {
                                          viewModel.deleteEntryByDateAndHabitId(
                                              habit.id,
                                              date.toEpochDay()
                                          )
                                      }) {
                                          Icon(
                                              Icons.Default.Check,
                                              contentDescription = ""
                                          )
                                      }
                                  }
                              }
                          }

                      }
//                        if (dates.indexOf(date) > dates.size - 5) {
//                            dates = dates + generateMoreDates(dates.last())
//                        }
//                        if (dates.indexOf(date) == 5) {
//                            dates = dates + generateMoreDatesInverse(dates.first())
//                            dates = dates.sortedByDescending{it}
//                        }
//                        if (dates.size > 60) {
//                            dates=  dates.takeLast(60).toMutableList()
//                        }
                  }
              }*/

        }
    }
}

private fun generateInitialDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (0L..14L).map { today.minusDays(it) }
}

fun fetchDates(initialDate: LocalDate, lastDate: LocalDate): List<LocalDate> {
    return generateSequence(initialDate) { it.minusDays(1) }
        .takeWhile { !it.isBefore(lastDate) }
        .toList()
}

// Generate more dates starting from the last date
private fun generateMoreDates(lastDate: LocalDate): List<LocalDate> {
    return (1L..14L).map { lastDate.minusDays(it) }
}

private fun generateMoreDatesInverse(firstDate: LocalDate): List<LocalDate> {
    return (1L..30L).map { firstDate.plusDays(it) }
}
