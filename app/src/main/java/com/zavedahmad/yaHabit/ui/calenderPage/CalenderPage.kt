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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import com.zavedahmad.yaHabit.ui.theme.Grey10
import com.zavedahmad.yaHabit.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalenderPage(viewModel: CalenderPageViewModel) {
    val habitData = viewModel.habitData.collectAsStateWithLifecycle().value
    val habitObject = viewModel.habitObject.collectAsStateWithLifecycle().value

    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    if (habitData == null || habitObject == null || themeReal== null ) {
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
                    calendarState.endMonth = calendarState.firstVisibleMonth.yearMonth.plusMonths(5)
                    calendarState.startMonth =
                        calendarState.firstVisibleMonth.yearMonth.minusMonths(5)
                }
                Box(Modifier.padding(innerPadding)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val partialAndAbsoluteCombinedList =
                            processDateTriples(findHabitClusters(habitData, 5, 3))
//                    Text(calendarState.firstVisibleMonth.yearMonth.toString())
                        MonthHeader(calendarState)
                        DaysOfWeekTitle(daysOfWeek)

                        HorizontalCalendar(
                            state = calendarState,

                            dayContent = { day ->
                                var dayState = ""
                                val dates = convertHabitCompletionEntityListToDatesList(habitData)
                                if (day.position == DayPosition.MonthDate) {
                                    if (dates.any { it == day.date }) {
                                        dayState = "absolute"
                                    } else if (partialAndAbsoluteCombinedList.any { it == day.date }) {
                                        dayState = "partial"
                                    }
                                } else {
                                    dayState = "disabled"
                                }

                                DayItem(day, dayState, viewModel)
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