package com.zavedahmad.yaHabit.ui.mainPage


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.utils.getAmoledThemeMode
import com.zavedahmad.yaHabit.database.utils.getFirstDayOfWeek
import com.zavedahmad.yaHabit.database.utils.getShowActive
import com.zavedahmad.yaHabit.database.utils.getShowArchive
import com.zavedahmad.yaHabit.database.utils.getTheme
import com.zavedahmad.yaHabit.ui.mainPage.HabitItemReorderable.HabitItemReorderableNew
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import kotlinx.coroutines.channels.Channel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPageReorderable(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    val listUpdatedChannel = remember { Channel<Unit>() }
    val habits = viewModel.habits.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isReorderableMode = viewModel.isReorderableMode.collectAsStateWithLifecycle()
    LaunchedEffect(habits.value) {
        listUpdatedChannel.trySend(Unit)
    }
    val allPreferences = viewModel.allPreferences.collectAsStateWithLifecycle().value


    if (allPreferences.isEmpty()) {
        ComposeTemplateTheme("system") {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }
    } else {


        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),


            topBar = {
                MainPageTopAppBar(
                    viewModel = viewModel,
                    backStack = backStack,
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            BottomSheetForFiltersAndSorting(viewModel)
            val currentHabits = habits.value
            val filteredHabits by remember(allPreferences, currentHabits) {
                derivedStateOf {
                    filterHabitsList(allPreferences.getShowArchive(),  allPreferences.getShowActive() , currentHabits)

                }
            }
            val lazyListState = rememberLazyListState()
            val reorderableLazyListState =
                rememberReorderableLazyListState(
                    lazyListState,

                    ) { from, to ->
                    listUpdatedChannel.tryReceive()
                    //println("from: key ${from.key} index ${from.index}  \n to:   key ${to.key} index ${to.index}")
                    val realFromIndex = filteredHabits[from.index - 1].index
                    val realToIndex = filteredHabits[to.index - 1].index
                    viewModel.move(realFromIndex, realToIndex)
                    listUpdatedChannel.receive()
                }


            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {


                if (filteredHabits.isEmpty()) {
                    NoHabitsPage(Modifier.padding(innerPadding))
                } else {
                    Box {

                        //HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            state = lazyListState,
//                            contentPadding = PaddingValues(top = 1.dp, start = 10.dp, end = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            item {  /* Button(onClick = {viewModel.move(1,3)}) { Text("Move") }*/ }
                            items(filteredHabits, key = { it.id }) { habit ->
                                ReorderableItem(
                                    reorderableLazyListState,
                                    key = habit.id
                                ) { isDragging ->
                                    CustomTheme(
                                        theme = allPreferences.getTheme(), // Ensure themeReal.value is not null here or provide a default
                                        primaryColor = habit.color,
                                        isAmoled = allPreferences.getAmoledThemeMode()
//
                                    ) {


                                        // Text("id :  ${habit.id.toString()}, index: ${habit.index}")

                                        HabitItemReorderableNew(
                                            backStack = backStack,
                                            viewModel = viewModel,
                                            habit = habit,
                                            reorderableListScope = this,
                                            isDragging = isDragging,
                                            isReorderableMode = isReorderableMode.value,
                                            firstDayOfWeek = allPreferences.getFirstDayOfWeek()
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

}

private fun filterHabitsList(showArchived : Boolean, showActive: Boolean, habits : List<HabitEntity>) : List<HabitEntity>{
    return habits.filter { showArchived && it.isArchived || showActive && !it.isArchived} .sortedBy { it.index }

}


