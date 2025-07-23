package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.habitsDetailPage.FullDataGridCalender
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import com.zavedahmad.yaHabit.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalenderPage(viewModel: CalendarPageViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val habitObject = viewModel.habitObject.collectAsStateWithLifecycle().value
    val habitAllData = viewModel.habitData.collectAsStateWithLifecycle().value
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    val options = listOf("Calendar", "Grid")
    val streakChecked = rememberSaveable { mutableStateOf(0) }
    if (habitObject == null || themeReal == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else {
        CustomTheme(theme = themeReal.value, primaryColor = habitObject.color) {
            Scaffold { innerPadding ->

                Column(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {


                    AnimatedVisibility(visible = streakChecked.value == 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(modifier = Modifier.width(400.dp)) {
                                MonthCalendarData(
                                    addHabit = { date ->
                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.addWithPartialCheck(
                                                HabitCompletionEntity(
                                                    habitId = viewModel.navKey.habitId,
                                                    completionDate = date
                                                )
                                            )
                                        }
                                    },
                                    deleteHabit = { date ->
                                        viewModel.deleteHabitEntryWithPartialCheck(
                                            habitId = habitObject.id,
                                            date = date
                                        )
                                    },
                                    habitData = habitAllData,
                                )

                            }
                        }
                    }
                    AnimatedVisibility(visible = streakChecked.value == 1) {
                        FullDataGridCalender(
                            addHabit = { date ->
                                coroutineScope.launch(
                                    Dispatchers.IO
                                ) {
                                    viewModel.habitRepository.addWithPartialCheck(
                                        HabitCompletionEntity(
                                            habitId = viewModel.navKey.habitId,
                                            completionDate = date
                                        )
                                    )
                                }
                            },
                            deleteHabit = { date ->
                                viewModel.deleteHabitEntryWithPartialCheck(
                                    habitId = habitObject.id,
                                    date = date
                                )
                            },
                            habitData = habitAllData,
                            gridHeight = 350,
                            showDate = true,
                            interactive = true,
                            firstDayOfWeek = DayOfWeek.SUNDAY,
                            skipHabit = {},
                            unSkipHabit = {},
                            dialogueComposable = {visible, onDismiss, habitCompletionEntity, completionDate ->  }
                        )
                    }
                    Row(Modifier.fillMaxWidth()) {
                        options.forEachIndexed { index, item ->
                            val isChecked = streakChecked.value == index
                            ToggleButton(
                                modifier = Modifier.weight(1f),
                                checked = isChecked, shapes = when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes(
                                        pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                        shape = ButtonGroupDefaults.connectedLeadingButtonPressShape
                                    )

                                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes(
                                        pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                        shape = ButtonGroupDefaults.connectedTrailingButtonPressShape
                                    )

                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes(
                                        pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                        shape = ButtonGroupDefaults.connectedMiddleButtonPressShape
                                    )
                                }, onCheckedChange = {
                                    if (!isChecked) {

                                        streakChecked.value = index
                                    }
                                })

                            {
                                Row {  /*AnimatedVisibility(visible = streakChecked.value == index) {
                                Icon(Icons.Default.Check, contentDescription = "selected", modifier = Modifier.size(15.dp))
                            }*/
                                    Text(item, overflow = TextOverflow.Ellipsis, maxLines = 1)
                                }
                            }
                        }
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
                Icons.Default.ArrowBackIosNew, contentDescription = "previous month"
            )
        }
        Text(currentMonth.toString())
        IconButton(onClick = {
            coroutineScope.launch {
                calendarState.scrollToMonth(currentMonth.plusMonths(1))

            }
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "next month"
            )
        }
    }
}