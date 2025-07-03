package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import com.zavedahmad.yaHabit.utils.findHabitClusters
import com.zavedahmad.yaHabit.utils.processDateTriples
import java.time.format.DateTimeFormatter
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HabitDetailsPage(viewModel: HabitDetailsPageViewModel) {
    val habitsPastYear = viewModel.habitsPastYear.collectAsStateWithLifecycle().value
    val habitDetails = viewModel.habitDetails.collectAsStateWithLifecycle().value
    val month = YearMonth.now()
    val twelveMonths = (0..12).map { month.minusMonths(it.toLong()) }

    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    if (themeReal == null) {
        ComposeTemplateTheme("system") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    } else {
        if (habitDetails == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }
        } else {
            CustomTheme(theme = themeReal.value, primaryColor = habitDetails.color) {
                Scaffold(topBar = { LargeTopAppBar(title = { Text(habitDetails.name) }) }) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(habitDetails.description)
                        Spacer(Modifier.height(30.dp))

//                        if (habitsPastYear != null) {
//                            Text(findHabitClusters(habitsPastYear, timeSpanDays = 5, 3).toString())
//                            Text(processDateTriples( findHabitClusters(habitsPastYear, timeSpanDays = 5, 3)).toString())
//                        }
                        Card {
                            Box(Modifier.padding(vertical = 20.dp)) {
                                LazyRow(reverseLayout = true) {
                                    item { Spacer(modifier = Modifier.width(10.dp)) }
                                    items(twelveMonths.size) { index ->
                                        val month = twelveMonths[index]
                                        Column(
                                            Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            val monthName =
                                                month.format(DateTimeFormatter.ofPattern("MMM"))
                                            Text(monthName)
                                            Box(
                                                modifier = Modifier
                                                    .height(100.dp)
                                                    .aspectRatio(1.0f)
                                                    .clickable(onClick = {})
                                            ) {
                                                Column(
                                                    Modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {

                                                    MonthGrid(month, habitsPastYear)
                                                }
                                            }
                                        }
                                    }
                                    item { Spacer(modifier = Modifier.width(10.dp)) }
                                }
                            }
                        }
                        Spacer(Modifier.height(40.dp))
                        Row (Modifier.fillMaxWidth()){
                            Card(Modifier   ) {
                                Column(
                                    Modifier

                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Past Month",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(habitsPastYear?.let { it.size.toString() } ?: "0",
                                        fontSize = 30.sp)
                                }
                            }
                            Card(Modifier    ) {
                                Column(
                                    Modifier

                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Past Year",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(habitsPastYear?.let { it.size.toString() } ?: "0",
                                        fontSize = 30.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun addMoreMonths(month: YearMonth): List<YearMonth> {
    return (0..12).map { month.minusMonths(it.toLong()) }
}