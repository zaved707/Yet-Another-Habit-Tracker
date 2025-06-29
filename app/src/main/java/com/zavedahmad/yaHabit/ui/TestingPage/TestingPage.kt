package com.zavedahmad.yaHabit.ui.TestingPage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon

import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestingPage(backStack: SnapshotStateList<NavKey>) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { MyMediumTopABCommon(backStack, scrollBehavior, "Habits") })
    { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val hapticFeedback = LocalHapticFeedback.current

            var list by remember { mutableStateOf(List(100) { "Item $it" }) }
            val lazyListState = rememberLazyListState()
            val reorderableLazyListState =
                rememberReorderableLazyListState(lazyListState) { from, to ->
                    list = list.toMutableList().apply {
                        add(to.index, removeAt(from.index))
                    }

                    hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(list, key = { it }) {
                    ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                        Surface(shadowElevation = elevation) {
                            Row {
                                Text(it, Modifier.padding(horizontal = 8.dp))
                                IconButton(
                                    modifier = Modifier.longPressDraggableHandle(),
                                    onClick = {},
                                ) {
                                    Icon(Icons.Rounded.DragHandle, contentDescription = "Reorder")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}