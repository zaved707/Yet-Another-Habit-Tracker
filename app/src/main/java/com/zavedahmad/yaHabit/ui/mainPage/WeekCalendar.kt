package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.ui.calenderPage.DayItem
import com.zavedahmad.yaHabit.ui.calenderPage.DaysOfWeekTitle
import java.time.LocalDate

@Composable
fun WeekCalendar(
    viewModel: MainPageViewModel,
    habit: HabitEntity
) {
    val completions = viewModel.getHabitCompletionsByHabitId(habit.id)
        .collectAsStateWithLifecycle(initialValue = emptyList()).value
    var dates by rememberSaveable { mutableStateOf(viewModel.generateInitialDates()) }
    val todayDate = LocalDate.now()
    val daysOfWeek = daysOfWeek()
    val state = rememberWeekCalendarState(
        startDate = todayDate.minusDays(10),
        endDate = todayDate.plusDays(10),
        firstVisibleWeekDate = todayDate
    )
    LaunchedEffect(state.firstVisibleWeek) {
        if(state.firstVisibleWeek.days.any{it.date == todayDate}){
            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)
            state.endDate = state.firstVisibleWeek.days.last().date
        }else {
            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)
            state.endDate = state.firstVisibleWeek.days.last().date.plusDays(14)
        }

    }
    DaysOfWeekTitle(daysOfWeek)
    WeekCalendar(dayContent = {day ->

        DayItem(day.date, state = "", addHabitEntry = {}, deleteHabit = {})  }, state = state)

}