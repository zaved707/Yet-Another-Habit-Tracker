package com.zavedahmad.yaHabit.ui.calenderPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthCalendarNew(


    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit,
    initialMonthString: String? = null,
    habitData: List<HabitCompletionEntity>?
) {
    val currentMonth =
        initialMonthString?.let { YearMonth.parse(initialMonthString) } ?: YearMonth.now()
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

        if (calendarState.firstVisibleMonth.yearMonth < YearMonth.now().minusMonths(5)) {
            calendarState.endMonth = calendarState.firstVisibleMonth.yearMonth.plusMonths(5)
        } else {
            calendarState.endMonth = YearMonth.now()
        }
        calendarState.startMonth = calendarState.firstVisibleMonth.yearMonth.minusMonths(5)
    }
    habitData?.let { habitData ->
        val dateToday = LocalDate.now()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            MonthHeader(calendarState)
            DaysOfWeekTitle(daysOfWeek)
            HorizontalCalendar(
                state = calendarState,
                dayContent = { day ->
                    var dayState = ""
                    val datesMatching = habitData.filter { it.completionDate == day.date }
                    if (datesMatching.size > 1 ) {
                        dayState = "error"
                    } else if(datesMatching.size == 1){
                        dayState = if (datesMatching[0].partial) {
                            if (day.position != DayPosition.MonthDate || day.date > dateToday) {
                                "partialDisabled"
                            } else {
                                "partial"
                            }
                        } else {
                            if (day.position != DayPosition.MonthDate || day.date.toEpochDay() > dateToday.toEpochDay()) {
                                "absoluteDisabled"
                            } else {
                                "absolute"
                            }
                        }
                    }else {
                        if (day.position != DayPosition.MonthDate || day.date > dateToday) {
                            dayState = "incompleteDisabled"
                        } else {
                            dayState = "incomplete"
                        }
                    }

                    DayItem(
                        date = day.date,
                        dayState,
                        addHabitEntry = { addHabit(day.date) },
                        deleteHabit = { deleteHabit(day.date) })
                }
            )

        }

    }

}