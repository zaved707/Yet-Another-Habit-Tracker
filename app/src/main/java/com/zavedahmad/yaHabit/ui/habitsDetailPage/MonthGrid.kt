package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import java.time.YearMonth


@Composable
fun MonthGrid(month: YearMonth, habitPastYear : List<HabitCompletionEntity>?) {
    val noOfDays = month.lengthOfMonth()
    val dates = (1..noOfDays).map { month.atDay(it) }
    LazyHorizontalGrid(
        modifier = Modifier

            , // Ensures the entire grid is square (1:1 aspect ratio)
        rows = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(month.atDay(1).dayOfWeek.value -1 ){

            Box(
                modifier = Modifier.clip(RoundedCornerShape(1.dp)).background(Color.Transparent)
                    .fillMaxSize()
                    .aspectRatio(1f), // Ensures each box is square
                contentAlignment = Alignment.Center
            ) {

            }
        }
        items(dates) {date ->
            var boxColor = MaterialTheme.colorScheme.onPrimary
            if(habitPastYear?.any { it.completionDate == date }?: false) {
                boxColor = MaterialTheme.colorScheme.primary
            }

            Box(
                modifier = Modifier.clip(RoundedCornerShape(1.dp)).background(boxColor)
                    .fillMaxSize()
                    .aspectRatio(1f), // Ensures each box is square
                contentAlignment = Alignment.Center
            ) {

            }
        }
        items(7- month.atDay(noOfDays).dayOfWeek.value  ){

            Box(
                modifier = Modifier.clip(RoundedCornerShape(1.dp)).background(Color.Transparent)
                    .fillMaxSize()
                    .aspectRatio(1f), // Ensures each box is square
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }
}