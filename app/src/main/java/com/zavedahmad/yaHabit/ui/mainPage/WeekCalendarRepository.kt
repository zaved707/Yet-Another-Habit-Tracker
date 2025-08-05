package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository

import com.zavedahmad.yaHabit.ui.components.DaysOfWeekTitle
import com.zavedahmad.yaHabit.database.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.database.utils.findHabitClusters
import com.zavedahmad.yaHabit.database.utils.processDateTriples
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekCalendar(

    habit: HabitEntity,
    habitRepository: HabitRepository,
    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit
) {
    val todayDate = LocalDate.now()
    val daysOfWeek = daysOfWeek()
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
    if (habitData.value == null){
        Column (Modifier.alpha(0.5f)){
            DaysOfWeekTitle(daysOfWeek)
            WeekCalendar(dayContent = {}, state = state) { }
        }

    }else{
    habitData.value?.let { habitData ->
        val dates = convertHabitCompletionEntityListToDatesList(habitData)
        val partialAndAbsoluteCombinedList =
            processDateTriples(findHabitClusters(habitData, habit.cycle, habit.frequency))
        val dateToday = LocalDate.now()
        Column {

        DaysOfWeekTitle(daysOfWeek)
        WeekCalendar(dayContent = { day ->
            var dayState = ""
           val datesMatching = habitData.filter { it.completionDate == day.date }
            if (datesMatching.size > 1) {
                dayState = "error"
            } else if (datesMatching.size == 1) {
                dayState = if (datesMatching[0].partial) {
                    if (day.date > dateToday) {
                        "partialDisabled"
                    } else {
                        "partial"
                    }
                } else {
                    if (day.date > dateToday) {
                        "absoluteDisabled"
                    } else {
                        "absolute"
                    }
                }
            } else {
                if (day.date > dateToday) {
                    dayState = "incompleteDisabled"
                } else {
                    dayState = "incomplete"
                }
            }




        }, state = state)

    }}}

}

