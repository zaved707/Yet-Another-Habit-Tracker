package com.zavedahmad.yaHabit.ui.TestingPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
import kotlinx.coroutines.launch
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TestingPage(backStack: SnapshotStateList<NavKey>) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { MyMediumTopABCommon(backStack, scrollBehavior, "Habits") })
    { innerPadding ->
//        LaunchedEffect(lazyRowState.firstVisibleItemIndex) { }
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val rowCount = 10
            val rowStates = List(rowCount) { rememberLazyListState() }
            val overscrollEffect = rememberOverscrollEffect()
            val stateRowX = rememberLazyListState() // State for the first Row, X
            val stateRowY = rememberLazyListState() // State for the second Row, Y
            val scope = rememberCoroutineScope()
            val scrollState = rememberScrollableState { delta ->
                scope.launch {
                    rowStates.forEach { state ->
                        state.scrollBy(-delta)
                    }

                }
                delta
            }

            LazyColumn(
                modifier = Modifier.scrollable(
                    overscrollEffect = overscrollEffect,
                    state = scrollState,
                    orientation = Orientation.Horizontal,
                    flingBehavior = ScrollableDefaults.flingBehavior()
                ).overscroll(overscrollEffect), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(rowCount) { index ->
                    LazyRow(Modifier.clickable(onClick = {}),
                        state = rowStates[index],
                        userScrollEnabled = false, horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(getInitialDates()) {
                            Card {
                                Column(
                                    Modifier
                                        .padding(10.dp)
                                        .width(30.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.dayOfMonth.toString())

                                }
                            }
                        }
                    }


                }

            }
        }
    }
}

fun getInitialDates(): MutableList<LocalDate> {
    val list = mutableListOf<LocalDate>()
    val dateNow = LocalDate.now()
    for (i in 0..100) {
        list.add(dateNow.minusDays(i.toLong()))

    }
    return list

}

@Composable
fun DualList() {

    val stateRowX = rememberLazyListState() // State for the first Row, X
    val stateRowY = rememberLazyListState() // State for the second Row, Y

    Column { // Placing two Lazy Rows one above the other for the example

        LazyRow(state = stateRowY) {
            items(LoremIpsum(10).values.toList()) {
                Text(it)
            }
        }

        LazyRow(state = stateRowX) {
            items(LoremIpsum(10).values.toList()) {
                Text(text = it)
            }
        }

    }

    //This might seem crazy

    LaunchedEffect(stateRowX.firstVisibleItemScrollOffset) {
        if (!stateRowY.isScrollInProgress) {
            stateRowY.scrollToItem(
                stateRowX.firstVisibleItemIndex,
                stateRowX.firstVisibleItemScrollOffset
            )
        }
    }

    LaunchedEffect(stateRowY.firstVisibleItemScrollOffset) {
        if (!stateRowX.isScrollInProgress) {
            stateRowX.scrollToItem(
                stateRowY.firstVisibleItemIndex,
                stateRowY.firstVisibleItemScrollOffset
            )
        }
    }
}