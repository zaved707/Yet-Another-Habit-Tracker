package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.HeatMapWeekHeaderPosition
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.calenderPage.MonthHeader
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun FullDataGridCalender(
    addHabit: (date: LocalDate) -> Unit = {},

    deleteHabit: (date: LocalDate) -> Unit = {},
    initialMonthString: String? = null,
    habitData: List<HabitCompletionEntity>? = null
) {
    val currentMonth = remember { YearMonth.now() }
//    val habitDataSorted = habitData.sortedBy { it.completionDate }
    val sortedDays =  habitData?.sortedBy { it.completionDate }

    val startMonth = remember {
        sortedDays?.first()?.completionDate?.yearMonth ?: currentMonth.minusMonths(12)
    } // Adjust as needed
    val lastDay =  sortedDays?.first()?.completionDate ?: LocalDate.now()
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val dateToday = LocalDate.now()
    val gridHeight = 180           // CHANGE this to change grid height


    val calendarState = rememberHeatMapCalendarState(
        startMonth = startMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )

    Card() {
        Box(
            Modifier
                .padding(vertical = 10.dp)
                .padding(end = 5.dp)
        ) {
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
                            fontSize = (gridHeight / 8 / 2).sp
                        )


                    }
                },
                monthHeader = {

                        if(LocalDate.now().yearMonth != it.yearMonth ){
                        Text(
                            it.yearMonth.month.toString(),
                            fontSize = (gridHeight / 8 / 3 * 2).sp, textAlign = TextAlign.Start
                        )
                    }}
                ,
                modifier = Modifier
                    .height(gridHeight.dp)
                    .fillMaxWidth(),
                state = calendarState,
                dayContent = { day, _ ->
                    var dayState = ""
                    if (habitData != null) {
                        val datesMatching = habitData.filter { it.completionDate == day.date }
                        if (datesMatching.size > 1) {
                            dayState = "error"
                        } else if (datesMatching.size == 1) {
                            dayState = if (datesMatching[0].partial) {
                                if ( day.date > dateToday) {
                                    "partialDisabled"
                                } else {
                                    "partial"
                                }
                            } else {
                                if ( day.date> dateToday) {
                                    "absoluteDisabled"
                                } else {
                                    "absolute"
                                }
                            }
                        }else {
                            if ( day.date > dateToday) {
                                dayState = "incompleteDisabled"
                            } else {
                                dayState = "incomplete"
                            }
                        }
                    }
                    if (dayState != "incompleteDisabled" && dayState != "absoluteDisabled" && dayState != "partialDisabled" ){
                    Box(
                        Modifier

                            .height((gridHeight / 8).dp)
                            .aspectRatio(1f),

                        ) {
                        Box(Modifier.padding((gridHeight / 80).dp)) {
                            GridDayItem(dayState, addHabit = {addHabit(day.date)}, deleteHabit ={deleteHabit(day.date)}, date = day.date)
                        }
                    }

                }})
        }
    }
}