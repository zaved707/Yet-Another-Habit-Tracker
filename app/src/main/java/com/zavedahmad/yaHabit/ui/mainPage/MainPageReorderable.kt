package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
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
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    LaunchedEffect(habits.value) {
        listUpdatedChannel.trySend(Unit)
    }

    if (themeReal == null) {
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

            topBar = { MyMediumTopABCommon(backStack, scrollBehavior, "Habits") },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = { backStack.add(Screen.AddHabitPageRoute()) }) {
                    Text("Add Habit")
                }
            }
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
                Button(onClick = {
                    coroutineScope.launch {
                        viewModel.habitRepository.addWithPartialCheck(
                            HabitCompletionEntity(habitId = 2, completionDate = LocalDate.parse("2025-06-18"))
                        )
                    }
                }) { Text("Add Entity With Partial Test") }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),

                    state = lazyListState,
                    contentPadding = PaddingValues(top = 1.dp, start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    items(habits.value, key = { it.id }) { habit ->
                        ReorderableItem(reorderableLazyListState, key = habit.id) { isDragging ->
                            CustomTheme(theme = themeReal.value, primaryColor = habit.color) {


                                // Text("id :  ${habit.id.toString()}, index: ${habit.index}")


                                HabitItemReorderable(
                                    backStack,
                                    viewModel,
                                    habit,
                                    reorderableListScope = this,
                                    isDragging
                                )

//                                Spacer(Modifier.height(40.dp))


                            }
                        }
                    }
                    item { Spacer(Modifier.height(70.dp)) }


                }
            }
        }
    }
}

