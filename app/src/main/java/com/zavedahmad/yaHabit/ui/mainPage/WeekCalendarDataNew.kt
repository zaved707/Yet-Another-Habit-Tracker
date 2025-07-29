package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.hasNote
import com.zavedahmad.yaHabit.roomDatabase.state

import com.zavedahmad.yaHabit.ui.components.DaysOfWeekTitle
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekCalendarDataNew(

    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit,
    initialWeekString: String? = null,
    habitEntity: HabitEntity,
    skipHabit: (date: LocalDate) -> Unit,
    habitData: List<HabitCompletionEntity>?,
    firstDayOfWeek: DayOfWeek,
    unSkipHabit: (date: LocalDate) -> Unit,
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


    val dateToday = LocalDate.now()
    Column {

        DaysOfWeekTitle(daysOfWeek(firstDayOfWeek = firstDayOfWeek))
        WeekCalendar(dayContent = { day ->

            var suffix = ""
            var hasNote = false
            var dayState = ""
            if (habitData != null) {
                val datesMatching = habitData.filter { it.completionDate == day.date }

                var habitCompletionEntity: HabitCompletionEntity? = null
                if (datesMatching.size > 1) {
                    dayState = "error"
                } else if (datesMatching.size == 1) {
                    hasNote = datesMatching[0].hasNote()
                    habitCompletionEntity = datesMatching[0]
                    suffix = if (day.date > dateToday) {
                        "Disabled"
                    } else {
                        ""
                    }
                    dayState = habitCompletionEntity.state()
                    if (dayState == "absolute"){
                        if(datesMatching[0].repetitionsOnThisDay < habitEntity.repetitionPerDay){
                            dayState = "absoluteLess"
                        }else if (datesMatching[0].repetitionsOnThisDay > habitEntity.repetitionPerDay){
                            dayState = "absoluteMore"
                        }
                    }
                    dayState += suffix

                } else {
                    if (day.date > dateToday) {
                        dayState = "incompleteDisabled"
                        suffix = "Disabled"
                    } else {
                        dayState = "incomplete"
                    }

                }
                val isDialogVisible = remember { mutableStateOf(false) }



                DayItem(
                    hasNote = hasNote,
                    repetitionsOnThisDay = if (dayState != "error" && datesMatching.size > 0) {
                        datesMatching[0].repetitionsOnThisDay
                    } else {
                        1.0
                    }, unSkipHabit = { unSkipHabit(day.date) },
                    date = day.date,
                    state = dayState,
                    skipHabit = { skipHabit(day.date) },
                    addHabit = {
                        addHabit(day.date)
                    },
                    deleteHabit = {
                        deleteHabit(day.date)
                    }, interactive = suffix != "Disabled",
                    dialogueComposable = { visible, onDismiss ->
                        dialogueComposable(visible, onDismiss, habitCompletionEntity, day.date)
                    })

            } else {DayItem(
                date = day.date,
                state = "incomplete",
                repetitionsOnThisDay = 0.0,
                skipHabit = {},
                unSkipHabit = {},
                dialogueComposable = {a,b -> }
            )}
        }, state = state)
    }
}


