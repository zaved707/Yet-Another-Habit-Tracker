package com.zavedahmad.yaHabit.ui.TestingPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable

fun ImplementationSingleScrollable(viewModel: MainPageViewModel) {
    val habits = viewModel.habits.collectAsStateWithLifecycle()
    val rowCount = 500
    val lazyColumnState = rememberLazyListState()
    val visibleRowStates = remember { mutableStateMapOf<Int, LazyListState>() }
    val overscrollEffect = rememberOverscrollEffect()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollableState { delta ->
        scope.launch {
            visibleRowStates.forEach { (_, state) ->
                state.scrollBy(-delta)
            }
        }
        delta
    }

    LaunchedEffect(lazyColumnState) {
        snapshotFlow { lazyColumnState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                // Update visibleRowStates based on currently visible items
                val visibleIndices = visibleItems.map { it.index }
                // Remove states for rows that are no longer visible
                visibleRowStates.keys.retainAll { it in visibleIndices }
                // Add states for newly visible rows
                visibleItems.forEach { item ->
                    if (!visibleRowStates.containsKey(item.index)) {
                        visibleRowStates[item.index] = LazyListState()
                    }
                }
            }
    }

    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier
            .scrollable(
                overscrollEffect = overscrollEffect,
                state = scrollState,
                orientation = Orientation.Horizontal,
                flingBehavior = ScrollableDefaults.flingBehavior()
            )
            .overscroll(overscrollEffect),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(rowCount) { index ->
            val rowState = visibleRowStates[index] ?: LazyListState()
            LazyRow(
                modifier = Modifier.clickable(onClick = {}),
                state = rowState,
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(getInitialDates()) { date ->
                    Box {
                        Column(
                            modifier = Modifier.width(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DayItemForTesting(
                                date = date,
                                state = "incomplete",
                                repetitionsOnThisDay = 0.0,
                                skipHabit = {},
                                unSkipHabit = {},
                                dialogueComposable = { a, b -> }
                            )
                        }
                    }
                }
            }
        }
    }
}