package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.calenderPage.DayItem
import com.zavedahmad.yaHabit.ui.calenderPage.DaysOfWeekTitle
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekCalendarDataNew(

    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit,
    initialWeekString: String? = null,
    habitData: List<HabitCompletionEntity>?,
    firstDayOfWeek: DayOfWeek,
    dialogueComposable: @Composable (Boolean, () -> Unit, HabitCompletionEntity?) -> Unit
) {
    val todayDate = LocalDate.now()
    val daysOfWeek = daysOfWeek()

    val state = rememberWeekCalendarState(
        startDate = todayDate.minusDays(10),
        endDate = todayDate,
        firstVisibleWeekDate = todayDate,
        firstDayOfWeek = firstDayOfWeek
    )


    LaunchedEffect(firstDayOfWeek) {

        state.firstDayOfWeek = firstDayOfWeek
        state.scrollToWeek(todayDate)

    }
    LaunchedEffect(state.firstVisibleWeek) {
        if (state.firstVisibleWeek.days.any { it.date == todayDate }) {
//            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        } else {
            state.startDate = state.firstVisibleWeek.days.first().date.minusDays(14)

        }

    }

    if (habitData == null) {
        Column(Modifier.alpha(0.5f)) {
            DaysOfWeekTitle(daysOfWeek)
            WeekCalendar(dayContent = { DayItem(date = it.date, state = "") }, state = state) { }
        }

    } else {


        val dateToday = LocalDate.now()
        Column {

            //DaysOfWeekTitle(daysOfWeek(firstDayOfWeek = firstDayOfWeek))
            WeekCalendar(dayContent = { day ->
                var dayState = ""
                val datesMatching = habitData.filter { it.completionDate == day.date }
                var habitCompletionEntity:  HabitCompletionEntity? = null
                if (datesMatching.size > 1) {
                    dayState = "error"
                } else if (datesMatching.size == 1) {
                    habitCompletionEntity = datesMatching[0]
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
                val isDialogVisible = remember { mutableStateOf(false) }



                DayItem3(
                    repetitionsOnThisDay = if (dayState != "error" && datesMatching.size > 0) {
                        datesMatching[0].repetitionsOnThisDay
                    } else {
                        1.0
                    },
                    date = day.date,
                    state = dayState,
                    addHabit = {
                        addHabit(day.date)
                    },
                    deleteHabit = {
                        deleteHabit(day.date)
                    },
                    dialogueComposable = { visible, onDismiss ->
                        dialogueComposable(visible, onDismiss,habitCompletionEntity)
                    })

            }, state = state)

        }
    }
}


