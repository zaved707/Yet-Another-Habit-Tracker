package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.HeatMapWeekHeaderPosition
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.yearMonth
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.hasNote
import com.zavedahmad.yaHabit.roomDatabase.state
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun FullDataGridCalender(
    addHabit: (date: LocalDate) -> Unit = {},

    deleteHabit: (date: LocalDate) -> Unit = {},
    initialMonthString: String? = null,
    habitData: List<HabitCompletionEntity>? = null, gridHeight: Int = 190,
    showDate: Boolean = false,
    interactive: Boolean = false,
    firstDayOfWeek: DayOfWeek,
    skipHabit: (date: LocalDate) -> Unit,
    unSkipHabit: (date: LocalDate) -> Unit,
    dialogueComposable: @Composable (Boolean, () -> Unit, HabitCompletionEntity?, LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
//    val habitDataSorted = habitData.sortedBy { it.completionDate }


    val startMonth = currentMonth.minusMonths(12)
    // Adjust as needed

    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    // Available from the library
    val dateToday = LocalDate.now()
    // CHANGE this to change grid height


    val calendarState = rememberHeatMapCalendarState(
        startMonth = startMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    LaunchedEffect(calendarState.firstVisibleMonth) {
        calendarState.startMonth = calendarState.firstVisibleMonth.yearMonth.minusMonths(12)

        if (calendarState.firstVisibleMonth.yearMonth < YearMonth.now().minusMonths(12)) {
            calendarState.endMonth = calendarState.firstVisibleMonth.yearMonth.plusMonths(12)
        } else {
            calendarState.endMonth = YearMonth.now()
        }
    }
if (habitData == null){}
else{
    Column {
        //Text("first visible Month ${calendarState.firstVisibleMonth.yearMonth} \n last visibleMonth: ${calendarState.lastVisibleMonth.yearMonth} \n startMonth ${calendarState.startMonth} \n endMonth ${calendarState.endMonth}")
        HeatMapCalendar(
            weekHeaderPosition = HeatMapWeekHeaderPosition.End,
            weekHeader = { weekDay ->
                Row(
                    Modifier

                        .height((gridHeight / 8).dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(Modifier.width(5.dp))
                    Text(
                        weekDay.name.slice(0..2),
                        fontSize = 15.sp
                    )


                }
            },
            monthHeader = {

                if (LocalDate.now().yearMonth != it.yearMonth) {
                    Column(
                        Modifier.height((gridHeight / 8).dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            it.yearMonth.month.toString().slice(0..2),
                            fontSize = 15.sp, textAlign = TextAlign.Start
                        )
                    }
                } else {
                    if (LocalDate.now().dayOfMonth > 15) {
                        Column(
                            Modifier.height((gridHeight / 8).dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                it.yearMonth.month.toString().slice(0..2),
                                fontSize = 15.sp, textAlign = TextAlign.Start
                            )
                        }
                    } else {
                        Spacer(Modifier.height((gridHeight / 8).dp))
                    }
                }
            },
            modifier = Modifier
                .height(gridHeight.dp)
                .fillMaxWidth(),
            state = calendarState,
            dayContent = { day, heatMapWeek ->
                var suffix = ""
                var hasNote = false
                var dayState = ""
                val datesMatching = habitData.filter { it.completionDate == day.date }
                var habitCompletionEntity:  HabitCompletionEntity? = null
                if (datesMatching.size > 1) {
                    dayState = "error"
                } else if (datesMatching.size == 1) {
                    hasNote = datesMatching[0].hasNote()
                    habitCompletionEntity = datesMatching[0]
                    suffix= if (day.date > dateToday){"Disabled"}else{""}
                    dayState = habitCompletionEntity.state() + suffix
                } else {
                    if (day.date > dateToday) {
                        dayState = "incompleteDisabled"
                        suffix = "Disabled"
                    } else {
                        dayState = "incomplete"
                    }

                }
                if ((dayState != "incompleteDisabled" && dayState != "absoluteDisabled" && dayState != "partialDisabled") || heatMapWeek.days.any { it.date == LocalDate.now() }) {
                    Box(
                        Modifier

                            .height((gridHeight / 8).dp)
                            .aspectRatio(1f),

                        ) {
                        Box(Modifier.padding((gridHeight / 80).dp)) {
                            GridDayItem(hasNote = hasNote,
                                state = dayState,
                                addHabit = { addHabit(day.date) },
                                deleteHabit = { deleteHabit(day.date) },
                                date = day.date,
                                showDate = showDate,
                                interactive =  suffix != "Disabled" && interactive,
                                dialogueComposable = { visible, onDismiss ->
                                    dialogueComposable(
                                        visible,
                                        onDismiss,
                                        habitCompletionEntity,
                                        day.date
                                    )
                                },
                                skipHabit = { skipHabit(day.date) },
                                unSkipHabit = { unSkipHabit(day.date) }
                            )
                        }
                    }

                }
            })


    }}
}