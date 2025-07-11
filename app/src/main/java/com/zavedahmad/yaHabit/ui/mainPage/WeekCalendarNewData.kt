package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.calenderPage.DayItem
import com.zavedahmad.yaHabit.ui.calenderPage.DaysOfWeekTitle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekCalendarNew(

    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit,
    initialWeekString: String? = null,
    habitData: List<HabitCompletionEntity>?
) {
    val todayDate = LocalDate.now()
    val daysOfWeek = daysOfWeek()

    val state = rememberWeekCalendarState(
        startDate = todayDate.minusDays(10),
        endDate = todayDate,
        firstVisibleWeekDate = todayDate
    )



    LaunchedEffect(state.firstVisibleWeek) {
        if (state.firstVisibleWeek.days.any { it.date == todayDate }) {
//            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        } else {
            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        }

    }

    if (habitData == null){
        Column (Modifier.alpha(0.5f)){
            DaysOfWeekTitle(daysOfWeek)
            WeekCalendar(dayContent = {DayItem(date = it.date, state = "")}, state = state) { }
        }

    }else{



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

