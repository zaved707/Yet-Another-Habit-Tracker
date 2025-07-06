package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.ui.calenderPage.DayItem
import com.zavedahmad.yaHabit.ui.calenderPage.DaysOfWeekTitle
import com.zavedahmad.yaHabit.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun WeekCalendar(

    habit: HabitEntity,
    habitRepository: HabitRepository,
    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit
) {
    val todayDate = LocalDate.now()

    val habitData = rememberSaveable { mutableStateOf<List<HabitCompletionEntity>?>(null) }
    val state = rememberWeekCalendarState(
        startDate = todayDate.minusDays(10),
        endDate = todayDate,
        firstVisibleWeekDate = todayDate
    )


    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(state.firstVisibleWeek) {
        if (state.firstVisibleWeek.days.any { it.date == todayDate }) {
//            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        } else {
            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        }

    }
    LaunchedEffect(Unit) {

        coroutineScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitCompletionsByIdFlow(habit.id).collect { habitData.value = it }
        }


    }
    habitData.value?.let { habitData ->
        val dates = convertHabitCompletionEntityListToDatesList(habitData)
        val partialAndAbsoluteCombinedList =
            processDateTriples(findHabitClusters(habitData, habit.cycle, habit.frequency))
        val dateToday = LocalDate.now()
        Column {
        val daysOfWeek = daysOfWeek()
        DaysOfWeekTitle(daysOfWeek)
        WeekCalendar(dayContent = { day ->
            var dayState = ""

            if (dates.any { it == day.date }) {
                if (day.date > dateToday) {
                    dayState = "absoluteDisabled"
                } else {
                    dayState = "absolute"
                }
            } else if (partialAndAbsoluteCombinedList.any { it == day.date }) {
                if (day.date > dateToday) {
                    dayState = "partialDisabled"
                } else {
                    dayState = "partial"
                }
            } else {
                if (day.date > dateToday) {
                    dayState = "incompleteDisabled"
                } else {
                    dayState = "incomplete"
                }
            }



            DayItem(
                day.date,
                state = dayState,
                addHabitEntry = {
                   addHabit(day.date)
                },
                deleteHabit = {
                    deleteHabit(day.date)
                })
        }, state = state)

    }}

}

