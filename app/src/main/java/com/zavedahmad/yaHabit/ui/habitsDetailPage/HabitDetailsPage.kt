package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.components.ConfirmationDialog
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HabitDetailsPage(viewModel: HabitDetailsPageViewModel,backStack: SnapshotStateList<NavKey>) {
    val habitsPastYear = viewModel.habitsPastYear.collectAsStateWithLifecycle().value
    val habitDetails = viewModel.habitDetails.collectAsStateWithLifecycle().value
    val month = YearMonth.now()
    val twelveMonths = (0..12).map { month.minusMonths(it.toLong()) }
    val habitAllData = viewModel.habitAllData.collectAsStateWithLifecycle().value
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    val coroutineScope = rememberCoroutineScope()
    val dialogueVisible = rememberSaveable { mutableStateOf(false) }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


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
                Scaffold(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
                    MediumFlexibleTopAppBar(
                        title = { Text(habitDetails.name, fontWeight = FontWeight.Bold) },
                        actions = {Menu(viewModel,backStack)},
                        scrollBehavior = scrollBehavior
                    )
                }) { innerPadding ->
                    if (dialogueVisible.value) {


                        Dialog(
                            onDismissRequest = { dialogueVisible.value = false },
                            properties = DialogProperties(
                                usePlatformDefaultWidth = false // This is the key change
                            )
                        ) {
                            Box(
                                Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                            ) {
                                FullDataGridCalender(
                                    addHabit = { date ->
                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.addWithPartialCheck(
                                                HabitCompletionEntity(
                                                    habitId = viewModel.navKey.habitId,
                                                    completionDate = date
                                                )
                                            )
                                        }
                                    },
                                    deleteHabit = { date ->
                                        viewModel.deleteHabitEntryWithPartialCheck(
                                            habitId = habitDetails.id,
                                            date = date
                                        )
                                    },
                                    habitData = habitAllData,
                                    gridHeight = 350,
                                    showDate = true,
                                    interactive = true
                                )
                            }
                        }
                    }
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Surface(tonalElevation = 20.dp) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                if (habitDetails.streakType == "everyday") {
                                    Text("EveryDay")
                                } else if (habitDetails.streakType == "week") {
                                    Text("${habitDetails.frequency} times per Week")
                                } else if (habitDetails.streakType == "month") {
                                    Text("${habitDetails.frequency} times per Month")
                                }else{Text("${habitDetails.frequency} times per ${habitDetails.cycle} Days")}
                            }
                        }
                        Column(
                            Modifier
                                .padding(horizontal = 10.dp)


                        ) {
                            if (habitDetails.description != "") {
                                Text(habitDetails.description)
                            }

                            Spacer(Modifier.height(10.dp))

                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable(onClick = { dialogueVisible.value = true })
                            ) {
                                FullDataGridCalender(
                                    habitData = habitAllData,
                                    addHabit = { date ->
                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.addWithPartialCheck(
                                                HabitCompletionEntity(
                                                    habitId = viewModel.navKey.habitId,
                                                    completionDate = date
                                                )
                                            )
                                        }
                                    },
                                    deleteHabit = { date ->
                                        viewModel.deleteHabitEntryWithPartialCheck(
                                            habitId = habitDetails.id,
                                            date = date
                                        )
                                    }
                                )
                            }

                            Spacer(Modifier.height(20.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(20.dp))
                            ColumnChartWidget(habitAllData)
                            Spacer(Modifier.height(20.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(20.dp))


                            PieChartDetail(habitAllData)
                            Spacer(Modifier.height(20.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(20.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    "Top Streaks",
                                    fontSize = 20.sp,
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                            Spacer(Modifier.height(10.dp))

                            StreakChartWidget(habitAllData)
                            Spacer(Modifier.height(40.dp))

//                            Spacer(Modifier.height(100.dp))
                            /* MonthCalendarNew(

                                 addHabit = { date ->
                                     coroutineScope.launch(
                                         Dispatchers.IO
                                     ) {
                                         viewModel.habitRepository.addWithPartialCheck(
                                             HabitCompletionEntity(
                                                 habitId = viewModel.navKey.habitId,
                                                 completionDate = date
                                             )
                                         )
                                     }
                                 },
                                 deleteHabit = { date ->
                                     viewModel.deleteHabitEntryWithPartialCheck(
                                         habitId = habitDetails.id,
                                         date = date
                                     )
                                 },
                                 habitData = habitAllData
                             )*/


                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Menu(viewModel: HabitDetailsPageViewModel,backStack: SnapshotStateList<NavKey>) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    ConfirmationDialog(
        visible = showDialog.value,
        text = "Do you want to delete this Habit?",
        confirmAction = {showDialog.value = false
            backStack.removeLastOrNull()
            viewModel.deleteHabitById(viewModel.navKey.habitId) },
        onDismiss = { showDialog.value = false },
        confirmationColor = MaterialTheme.colorScheme.error
    )

    val menuVisible = rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { menuVisible.value = !menuVisible.value }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "More"
        )
    }
    DropdownMenu(
        expanded = menuVisible.value, // Set to true to show the menu
        onDismissRequest = { menuVisible.value = false }
    ) {
        DropdownMenuItem(text = {
            Row {
                Text("Edit Habit")

            }
        }, onClick = { menuVisible.value = false
            backStack.add(Screen.AddHabitPageRoute(viewModel.navKey.habitId))})
        DropdownMenuItem(text = {
            Row {
                Text("Delete Habit")

            }
        }, onClick = { menuVisible.value = false
        showDialog.value  = true})
    }
}