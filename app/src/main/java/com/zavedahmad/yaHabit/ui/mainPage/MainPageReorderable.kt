package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
import com.zavedahmad.yaHabit.ui.errorPages.SplashScreen
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPageReorderable(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    val listUpdatedChannel = remember { Channel<Unit>() }
    val habits = viewModel.habits.collectAsStateWithLifecycle()
    val firstDayOfWeek = viewModel.firstDayOfWeek.collectAsStateWithLifecycle().value
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isAmoledColor by viewModel.amoledTheme.collectAsStateWithLifecycle()
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    val isReorderableMode = viewModel.isReorderableMode.collectAsStateWithLifecycle()
    LaunchedEffect(habits.value) {
        listUpdatedChannel.trySend(Unit)
    }

    if (themeReal == null || isAmoledColor == null || firstDayOfWeek == null) {
        ComposeTemplateTheme("system") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    } else {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                MediumFlexibleTopAppBar(
                    title = {
                        Text(
                            "Habits",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {

                        if (isReorderableMode.value
                        ) {
                            Button(
                                onClick = {


                                    viewModel.changeReorderableMode(false)

                                },
                                modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "turn off reorderable mode"
                                )
                            }
                        }

                        if (!isReorderableMode.value
                        ) {
                            Row {
                                Button(
                                    onClick = {


                                        backStack.add(Screen.AddHabitPageRoute())

                                    },
                                    modifier = Modifier,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        Icons.Default.AddCircle,
                                        contentDescription = "turn off reorderable mode"
                                    )
                                }
                                Menu(viewModel, backStack)
                            }
                        }


                    },

                    scrollBehavior = scrollBehavior
                )
            },
            /* floatingActionButton = {
                 AnimatedVisibility(
                     visible = !isReorderableMode.value,
                     enter = fadeIn(),
                     exit = fadeOut()
                 ) {
                     ExtendedFloatingActionButton(onClick = { backStack.add(Screen.AddHabitPageRoute()) }) {
                         Text("Add Habit")
                     }
                 }
             }*/
        ) { innerPadding ->

            val lazyListState = rememberLazyListState()
            val reorderableLazyListState =
                rememberReorderableLazyListState(
                    lazyListState,

                    ) { from, to ->
                    listUpdatedChannel.tryReceive()
//        viewModel.moveHabits(from.index,to.index)
                    //println("from: key ${from.key} index ${from.index}  \n to:   key ${to.key} index ${to.index}")

                    viewModel.move(from.index, to.index)
                    listUpdatedChannel.receive()
                }
            // Button(onClick = {viewModel.move(5 ,6)}) {Text("MOve") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val pagerState = rememberPagerState(pageCount = {
                    1
                })

                if (habits.value.isEmpty()) {
                    VerticalPager(state = pagerState) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.FormatListBulleted,
                                modifier = Modifier.size(200.dp),
                                contentDescription = "placeholderr",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                "You Have not Yet added any Habits \n click the '+' button  to add one",
                                textAlign = TextAlign.Center
                            )

                        }
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),

                        state = lazyListState,
                        contentPadding = PaddingValues(top = 1.dp, start = 10.dp, end = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        items(habits.value, key = { it.id }) { habit ->
                            ReorderableItem(
                                reorderableLazyListState,
                                key = habit.id
                            ) { isDragging ->
                                CustomTheme(
                                    theme = themeReal.value,
                                    primaryColor = habit.color,
                                    isAmoled = isAmoledColor?.value == "true"
                                ) {


                                    // Text("id :  ${habit.id.toString()}, index: ${habit.index}")


                                    HabitItemReorderableNew(
                                        backStack = backStack,
                                        viewModel = viewModel,
                                        habit = habit,
                                        reorderableListScope = this,

                                        isDragging = isDragging,
                                        isReorderableMode = isReorderableMode.value,
                                        firstDayOfWeek = firstDayOfWeek
                                    )

//                                Spacer(Modifier.height(40.dp))


                                }
                            }
                        }
                        item { Spacer(Modifier.height(20.dp)) }


                    }
                }
            }
        }
    }
}

@Composable
private fun Menu(viewModel: MainPageViewModel, backStack: SnapshotStateList<NavKey>) {
    val isReorderableMode = viewModel.isReorderableMode.collectAsStateWithLifecycle()
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
        DropdownMenuItem(text = { Row { Text("Settings") } }, onClick = {
            menuVisible.value = false
            backStack.add(Screen.SettingsPageRoute)
        })
        DropdownMenuItem(text = {
            Row {
                Text("Reorder Mode")

            }
        }, onClick = {
            viewModel.changeReorderableMode(!isReorderableMode.value)
            menuVisible.value = false
        })
    }
}