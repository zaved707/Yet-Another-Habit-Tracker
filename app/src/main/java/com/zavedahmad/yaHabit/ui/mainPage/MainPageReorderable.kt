package com.zavedahmad.yaHabit.ui.mainPage


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

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
        val colorForBorder = MaterialTheme.colorScheme.outlineVariant.copy(0.5f)

        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),


            topBar = {
                MediumFlexibleTopAppBar(
                    modifier = Modifier.drawWithContent() {
                        drawContent()
                        drawLine(
                            color = colorForBorder,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                    title = {
                        Text( // Add border around this
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
                                OutlinedButton(
                                    onClick = {


                                        backStack.add(Screen.AddHabitPageRoute())

                                    },
                                    modifier = Modifier,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                            0.5f
                                        )
                                    ),
                                    contentPadding = PaddingValues(
                                        vertical = 2.dp,
                                        horizontal = 20.dp
                                    )
                                ) {
                                    Box(
                                        Modifier
                                            .clip(MaterialShapes.Cookie12Sided.toShape())
                                            .border(
                                                border = BorderStroke(
                                                    width = 2.dp,
                                                    brush = SolidColor(
                                                        MaterialTheme.colorScheme.primary.copy(
                                                            0.5f
                                                        )
                                                    )
                                                ), shape = MaterialShapes.Cookie12Sided.toShape()
                                            )
                                            .background(MaterialTheme.colorScheme.primary.copy(0.7f))
                                    ) {
                                        Box(Modifier.padding(5.dp)) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "add new Item",
                                                tint = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                }
                                MainPageMenu(viewModel, backStack)
                            }
                        }


                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),

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

                    viewModel.move(from.index - 1, to.index - 1)
                    listUpdatedChannel.receive()
                }
            // Button(onClick = {viewModel.move(5 ,6)}) {Text("MOve") }


            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {




                if (habits.value.isEmpty()) {
                    val pagerState = rememberPagerState(pageCount = {
                        1
                    })
                    VerticalPager(state = pagerState) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.FormatListBulleted,
                                modifier = Modifier.size(200.dp),
                                contentDescription = "placeholderr",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "You Have not Yet added any Habits \n click the '+' button  to add one",
                                textAlign = TextAlign.Center
                            )


                        }
                    }
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
                            item { }
                            items(habits.value, key = { it.id }) { habit ->
                                ReorderableItem(
                                    reorderableLazyListState,
                                    key = habit.id
                                ) { isDragging ->
                                    CustomTheme(
                                        theme = themeReal.value, // Ensure themeReal.value is not null here or provide a default
                                        primaryColor = habit.color,
                                        isAmoled = false
//                                        isAmoled = isAmoledColor?.value == "true"  // Todo temporarily removing amoled
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

}

@Composable
fun ExportDatabase(viewModel: MainPageViewModel ) {
    val context = LocalContext.current
    val exportLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/x-sqlite3")
        ) { uri: Uri? ->
            uri?.let {
                viewModel.exportDatabase(context, it) { result ->
                    result.onSuccess {
                        makeToast(context,"databaseExported")
                    }.onFailure { e ->
                        makeToast(context,"failure")
                    }
                }
            }


        }
    Button(onClick = { exportLauncher.launch("main_database.db") }) {
        Text("Export Database")
    }

}


fun makeToast(context : Context, text : String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

    }
}
