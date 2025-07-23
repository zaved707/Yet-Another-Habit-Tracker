package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.components.ConfirmationDialog
import com.zavedahmad.yaHabit.ui.mainPage.DialogueForHabit
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HabitDetailsPage(viewModel: HabitDetailsPageViewModel, backStack: SnapshotStateList<NavKey>) {
    val habitsPastYear = viewModel.habitsPastYear.collectAsStateWithLifecycle().value
    val habit = viewModel.habitDetails.collectAsStateWithLifecycle().value
    val month = YearMonth.now()
    val firstDayOfWeek = viewModel.firstDayOfWeek.collectAsStateWithLifecycle().value
    val twelveMonths = (0..12).map { month.minusMonths(it.toLong()) }
    val habitAllData = viewModel.habitAllData.collectAsStateWithLifecycle().value
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    val isAmoledColor by viewModel.amoledTheme.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    val dialogueVisible = rememberSaveable { mutableStateOf(false) }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


    if (themeReal == null || isAmoledColor == null || firstDayOfWeek == null) {
        ComposeTemplateTheme("system") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    } else {
        if (habit == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }
        } else {
            CustomTheme(
                theme = themeReal.value,
                primaryColor = habit.color,
                isAmoled = isAmoledColor?.value == "true"
            ) {
                Scaffold(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
                    MediumFlexibleTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { backStack.removeLastOrNull() }) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "go back"
                                )
                            }
                        },
                        title = { Text(habit.name, fontWeight = FontWeight.Bold) },
                        actions = { Menu(viewModel, backStack) },
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
                                    gridHeight = if (LocalConfiguration.current.screenHeightDp < 350) {
                                        LocalConfiguration.current.screenHeightDp
                                    } else {
                                        350
                                    },
                                    addHabit = { date ->

                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.applyRepetitionForADate(
                                                date = date,
                                                habitId = habit.id,
                                                newRepetitionValue = habit.repetitionPerDay
                                            )
                                        }
                                    },
                                    deleteHabit = { date ->
                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.applyRepetitionForADate(
                                                date = date,
                                                habitId = habit.id,
                                                newRepetitionValue = 0.0
                                            )
                                        }
                                    },
                                    habitData = habitAllData,
                                    showDate = true,
                                    interactive = true,
                                    firstDayOfWeek = firstDayOfWeek,
                                    dialogueComposable = { visible, onDismiss, habitCompletionEntity, completionDate ->
                                        DialogueForHabit(
                                            isVisible = visible,
                                            onDismissRequest = { onDismiss() },
                                            habitCompletionEntity = habitCompletionEntity,
                                            updateHabitCompletionEntity = { habitCompletionEntity ->

                                            },
                                            habitEntity = habit,
                                            onFinalised = { isRepetitionsChanged, isNotesChanged, userTypedRepetition, userTypedNote ->
                                                if (isRepetitionsChanged && userTypedRepetition.toDoubleOrNull() != null) {
                                                    coroutineScope.launch(Dispatchers.IO) {
                                                        viewModel.habitRepository.applyRepetitionForADate(
                                                            date = completionDate,
                                                            habitId = habit.id,
                                                            newRepetitionValue = userTypedRepetition.toDouble()
                                                        )
                                                        if (isNotesChanged) {
                                                            viewModel.habitRepository.applyNotes(
                                                                date = completionDate,
                                                                habitId = habit.id,
                                                                newNote = userTypedNote
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    if (isNotesChanged) {
                                                        coroutineScope.launch(Dispatchers.IO) {
                                                            viewModel.habitRepository.applyNotes(
                                                                date = completionDate,
                                                                habitId = habit.id,
                                                                newNote = userTypedNote
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        )
                                    },skipHabit = { date ->
                                        coroutineScope.launch(Dispatchers.IO) {
                                            viewModel.habitRepository.setSkip(
                                                date = date,
                                                habitId = habit.id,
                                                skipValue = true
                                            )
                                        }
                                    }, unSkipHabit = { date ->
                                        coroutineScope.launch {
                                            viewModel.habitRepository.setSkip(
                                                date = date,
                                                habitId = habit.id,
                                                skipValue = false
                                            )
                                        }
                                    },

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
                                Icon(Icons.Default.Repeat, "")
                                Spacer(Modifier.width(10.dp))
                                if (habit.streakType == "everyday") {
                                    Text("EveryDay")
                                } else if (habit.streakType == "week") {
                                    Text("${habit.frequency} times per Week")
                                } else if (habit.streakType == "month") {
                                    Text("${habit.frequency} times per Month")
                                } else {
                                    Text("${habit.frequency} times per ${habit.cycle} Days")
                                }
                            }
                        }
                        Column(
                            Modifier
                                .padding(horizontal = 10.dp)


                        ) {
                            if (habit.description != "") {
                                Text(habit.description)
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
                                            viewModel.habitRepository.applyRepetitionForADate(
                                                date = date,
                                                habitId = habit.id,
                                                newRepetitionValue = habit.repetitionPerDay
                                            )
                                        }
                                    },
                                    deleteHabit = { date ->
                                        coroutineScope.launch(
                                            Dispatchers.IO
                                        ) {
                                            viewModel.habitRepository.applyRepetitionForADate(
                                                date = date,
                                                habitId = habit.id,
                                                newRepetitionValue = 0.0
                                            )
                                        }
                                    },
                                    firstDayOfWeek = firstDayOfWeek,



                                    interactive = false,
                                    skipHabit = { } ,
                                    unSkipHabit = {},
                                    dialogueComposable = {visible, onDismiss, habitCompletionEntity, completionDate -> },
                                )
                            }

                            Spacer(Modifier.height(20.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(20.dp))
                            FrequencyChart(habitAllData)
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
private fun Menu(viewModel: HabitDetailsPageViewModel, backStack: SnapshotStateList<NavKey>) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    ConfirmationDialog(
        visible = showDialog.value,
        text = "Do you want to delete this Habit?",
        confirmAction = {
            showDialog.value = false
            backStack.removeLastOrNull()
            viewModel.deleteHabitById(viewModel.navKey.habitId)
        },
        onDismiss = { showDialog.value = false },
        confirmationColor = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onError,
            containerColor = MaterialTheme.colorScheme.error
        )
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
        }, onClick = {
            menuVisible.value = false
            backStack.add(Screen.AddHabitPageRoute(viewModel.navKey.habitId))
        })
        DropdownMenuItem(text = {
            Row {
                Text("Delete Habit")

            }
        }, onClick = {
            menuVisible.value = false
            showDialog.value = true
        })
    }
}