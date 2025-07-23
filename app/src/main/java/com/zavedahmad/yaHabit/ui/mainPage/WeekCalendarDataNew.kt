package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.hasNote
import com.zavedahmad.yaHabit.roomDatabase.state
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
    skipHabit : (date : LocalDate) -> Unit,
    habitData: List<HabitCompletionEntity>?,
    firstDayOfWeek: DayOfWeek,
    unSkipHabit:(date: LocalDate)-> Unit ,
    dialogueComposable: @Composable (Boolean, () -> Unit, HabitCompletionEntity?, LocalDate) -> Unit
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
                var hasNote = false
                var dayState = ""
                val datesMatching = habitData.filter { it.completionDate == day.date }
                var habitCompletionEntity:  HabitCompletionEntity? = null
                if (datesMatching.size > 1) {
                    dayState = "error"
                } else if (datesMatching.size == 1) {
                    hasNote = datesMatching[0].hasNote()
                    habitCompletionEntity = datesMatching[0]
                    val suffix= if (day.date > dateToday){"Disabled"}else{""}
                   dayState = habitCompletionEntity.state() + suffix
                } else {
                    if (day.date > dateToday) {
                        dayState = "incompleteDisabled"
                    } else {
                        dayState = "incomplete"
                    }

                }
                val isDialogVisible = remember { mutableStateOf(false) }



                DayItem(hasNote = hasNote ,
                    repetitionsOnThisDay = if (dayState != "error" && datesMatching.size > 0) {
                        datesMatching[0].repetitionsOnThisDay
                    } else {
                        1.0
                    }, unSkipHabit = {unSkipHabit(day.date)},
                    date = day.date,
                    state = dayState,
                    skipHabit ={ skipHabit(day.date)},
                    addHabit = {
                        addHabit(day.date)
                    },
                    deleteHabit = {
                        deleteHabit(day.date)
                    },
                    dialogueComposable = { visible, onDismiss ->
                        dialogueComposable(visible, onDismiss,habitCompletionEntity, day.date)
                    })

            }, state = state)

        }
    }
}


