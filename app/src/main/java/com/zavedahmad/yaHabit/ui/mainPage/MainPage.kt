package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    var dates by remember { mutableStateOf(generateInitialDates()) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyRow {
            items(dates) { date ->
                Column (modifier = Modifier.padding(20.dp)){
                    Text(
                        date.dayOfMonth.toString()
                    )
                    Text(date.month.toString())
                }

            }
        }
    }
}

private fun generateInitialDates(): List<LocalDate> {
    val today = LocalDate.now()
    return (0L..30L).map { today.minusDays(it) }
}

// Generate more dates starting from the last date
private fun generateMoreDates(lastDate: LocalDate): List<LocalDate> {
    return (1L..30L).map { lastDate.minusDays(it) }
}
