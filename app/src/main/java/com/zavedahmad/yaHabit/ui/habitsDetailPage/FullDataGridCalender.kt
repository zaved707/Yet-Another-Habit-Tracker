package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.ui.calenderPage.DaysOfWeekTitle
import com.zavedahmad.yaHabit.ui.calenderPage.MonthHeader
import com.zavedahmad.yaHabit.utils.convertHabitCompletionEntityListToDatesList
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun FullDataGridCalender(
    habit: HabitEntity,
    habitRepository: HabitRepository,
    addHabit: (date: LocalDate) -> Unit,
    deleteHabit: (date: LocalDate) -> Unit,
    initialMonthString: String? = null
) {
    val todayDate = LocalDate.now()
    val coroutineScope = rememberCoroutineScope()
    val habitData = rememberSaveable { mutableStateOf<List<HabitCompletionEntity>?>(null) }

    val currentMonth =
        initialMonthString?.let { YearMonth.parse(initialMonthString) } ?: YearMonth.now()
    val startMonth = currentMonth.minusMonths(10)
    val endMonth = currentMonth.plusMonths(10)
    val daysOfWeek = daysOfWeek()
    val calendarState = rememberHeatMapCalendarState(
        endMonth = endMonth,
        startMonth = startMonth,
        firstVisibleMonth = currentMonth,


        firstDayOfWeek = daysOfWeek.first(),
    )
    LaunchedEffect(calendarState.firstVisibleMonth) {
        println(calendarState.firstVisibleMonth)
        if (calendarState.firstVisibleMonth.yearMonth < YearMonth.now().minusMonths(5)) {
            calendarState.endMonth = calendarState.firstVisibleMonth.yearMonth.plusMonths(5)
        } else {
            calendarState.endMonth = YearMonth.now()
        }
        calendarState.startMonth = calendarState.firstVisibleMonth.yearMonth.minusMonths(5)
    }
    LaunchedEffect(Unit) {

        coroutineScope.launch(Dispatchers.IO) {
            habitRepository.getAllHabitEntriesById(habit.id).collect { habitData.value = it }
        }


    }
    habitData.value?.let { habitData ->
        val dates = convertHabitCompletionEntityListToDatesList(habitData)
        val partialAndAbsoluteCombinedList =
            processDateTriples(findHabitClusters(habitData, habit.cycle, habit.frequency))
        val dateToday = LocalDate.now()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            DaysOfWeekTitle(daysOfWeek)

        }
        HeatMapCalendar(state = calendarState, dayContent ={day, _ -> Text( text =  day.date.dayOfMonth.toString()) })
    }
}