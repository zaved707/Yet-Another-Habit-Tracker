package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import com.zavedahmad.yaHabit.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalenderPage(viewModel: CalendarPageViewModel) {
    val habitData = viewModel.habitData.collectAsStateWithLifecycle().value
    val habitObject = viewModel.habitObject.collectAsStateWithLifecycle().value

    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    if (habitData == null || habitObject == null || themeReal == null) {
        LoadingIndicator()
    } else {
        CustomTheme(theme = themeReal.value, primaryColor = habitObject.color) {
            Scaffold { innerPadding ->
                val coroutineScope = rememberCoroutineScope()
                val currentMonth = YearMonth.parse(viewModel.navKey.month)
                val startMonth = currentMonth.minusMonths(10)
                val endMonth = currentMonth.plusMonths(10)
                val daysOfWeek = daysOfWeek()
                val calendarState = rememberCalendarState(
                    endMonth = endMonth,
                    startMonth = startMonth,
                    firstVisibleMonth = currentMonth, outDateStyle = OutDateStyle.EndOfGrid,


                    firstDayOfWeek = daysOfWeek.first(),
                )
                LaunchedEffect(calendarState.firstVisibleMonth) {
                    println(calendarState.firstVisibleMonth)
                    if (calendarState.firstVisibleMonth.yearMonth < YearMonth.now()
                            .minusMonths(5)
                    ) {
                        calendarState.endMonth =
                            calendarState.firstVisibleMonth.yearMonth.plusMonths(5)
                    } else {
                        calendarState.endMonth =
                            YearMonth.now()
                    }
                    calendarState.startMonth =
                        calendarState.firstVisibleMonth.yearMonth.minusMonths(5)
                }
                Box(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
// this list does not contains all absolute values it only contain absolute values which are a part of a streak
                        val dates = convertHabitCompletionEntityListToDatesList(habitData)
                        val partialAndAbsoluteCombinedList =
                            processDateTriples(findHabitClusters(habitData, 5, 3))
                       val dateToday = LocalDate.now()
//                    Text(calendarState.firstVisibleMonth.yearMonth.toString())


                        MonthHeader(calendarState)
                        DaysOfWeekTitle(daysOfWeek)

                        HorizontalCalendar(
                            state = calendarState,

                            dayContent = { day ->
                                var dayState = ""

                                if (dates.any { it == day.date }) {
                                    if (day.position != DayPosition.MonthDate || day.date.toEpochDay() > dateToday.toEpochDay()) {
                                        dayState = "absoluteDisabled"
                                    } else {
                                        dayState = "absolute"
                                    }
                                } else if (partialAndAbsoluteCombinedList.any { it == day.date }) {
                                    if (day.position != DayPosition.MonthDate || day.date > dateToday) {
                                        dayState = "partialDisabled"
                                    } else {
                                        dayState = "partial"
                                    }
                                } else {
                                    if (day.position != DayPosition.MonthDate || day.date > dateToday) {
                                        dayState = "incompleteDisabled"
                                    } else {
                                        dayState = "incomplete"
                                    }
                                }


                                DayItem(
                                    day.date,
                                    dayState,
                                    addHabitEntry = {
                                        viewModel.addHabitEntry(
                                            completionDate = day.date,
                                            habitId = habitObject.id
                                        )
                                    },
                                    deleteHabit = {
                                        viewModel.deleteEntryByDateAndHabitId(
                                            habitId = habitObject.id,
                                            date = day.date.toEpochDay()
                                        )
                                    })
                            })
                    }

                }
            }
        }
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@Composable
fun MonthHeader(calendarState: CalendarState) {
    val coroutineScope = rememberCoroutineScope()
    val currentMonth = calendarState.firstVisibleMonth.yearMonth
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {

            coroutineScope.launch {
                calendarState.scrollToMonth(currentMonth.minusMonths(1))

            }
        }) {
            Icon(
                Icons.Default.ArrowBackIosNew,
                contentDescription = "previous month"
            )
        }
        Text(currentMonth.toString())
        IconButton(onClick = {
            coroutineScope.launch {
                calendarState.scrollToMonth(currentMonth.plusMonths(1))

            }
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "next month"
            )
        }
    }
}