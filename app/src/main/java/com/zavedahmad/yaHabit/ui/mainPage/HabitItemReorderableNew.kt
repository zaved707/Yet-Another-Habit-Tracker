package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.ui.components.ConfirmationDialog
import com.zavedahmad.yaHabit.ui.theme.LocalOutlineSizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HabitItemReorderableNew(
    backStack: SnapshotStateList<NavKey>,
    viewModel: MainPageViewModel,
    habit: HabitEntity,
    reorderableListScope: ReorderableCollectionItemScope? = null,
    isDragging: Boolean = false,
    isReorderableMode: Boolean = false,
    firstDayOfWeek: DayOfWeek
) {

    val coroutineScope = rememberCoroutineScope()
    val color = if (isDragging) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)

    } else {
        CardDefaults.cardColors()
    }
    val cardElevation = if (isDragging) {
        CardDefaults.outlinedCardElevation(defaultElevation = 10.dp)

    } else {
        CardDefaults.outlinedCardElevation()
    }
    val habitData = rememberSaveable { mutableStateOf<List<HabitCompletionEntity>?>(null) }
    val showDialog = rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {

        coroutineScope.launch(Dispatchers.IO) {
            viewModel.habitRepository.getAllHabitCompletionsByIdFlow(habit.id)
                .collect { habitData.value = it }
        }


    }
    Card(
        modifier =
            Modifier
                .fillMaxWidth().padding(horizontal = 10.dp),
        elevation = cardElevation,

        colors = color,
        border = BorderStroke(width = LocalOutlineSizes.current.small, color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f)),
        onClick = {
            if (!isReorderableMode) {
                backStack.add(Screen.HabitDetailsPageRoute(habit.id))
            }
        })
    {
        ConfirmationDialog(
            visible = showDialog.value,
            text = "Do you want to delete this Habit?",
            confirmAction = { viewModel.deleteHabitById(habit.id) },
            onDismiss = { showDialog.value = false },
            confirmationColor = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onError,
                containerColor = MaterialTheme.colorScheme.error
            )
        )
        Column(
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier =

                    Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Column(Modifier.fillMaxWidth(0.7f)) {
                    Text(
                        habit.name,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    if (habit.description != "") {

                        Text(
                            habit.description,

                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                    }
                }
                if (isReorderableMode) {
                    IconButton(
                        onClick = {},
                        modifier = if (reorderableListScope != null) {
                            with(reorderableListScope) {
                                Modifier
                                    .draggableHandle()
                            }
                        } else {
                            Modifier
                        }

                    ) {
                        Icon(Icons.Default.DragHandle, contentDescription = "Reorder")
                    }
                }
                /*IconButton(
                    modifier = with(reorderableListScope) {
                        Modifier.draggableHandle(

                        )
                    },
                    onClick = {},
                ) {
                    Icon(
                        Icons.Rounded.DragHandle,
                        contentDescription = "Reorder"
                    )
                }*/


            }
            AnimatedVisibility(visible = !isReorderableMode) {
                Column(Modifier.fillMaxWidth()) {

                    WeekCalendarDataNew(
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
                        habitData = habitData.value,
                        firstDayOfWeek = firstDayOfWeek,
                        skipHabit = { date ->
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.habitRepository.setSkip(
                                    date = date,
                                    habitId = habit.id,
                                    skipValue = true
                                )
                            }
                        },
                        unSkipHabit = { date ->
                            coroutineScope.launch {
                                viewModel.habitRepository.setSkip(
                                    date = date,
                                    habitId = habit.id,
                                    skipValue = false
                                )
                            }
                        }, habitEntity = habit,
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
                        }
                    )


                    Spacer(Modifier.height(20.dp))

                    HorizontalDivider(color =  MaterialTheme.colorScheme.outlineVariant.copy(0.5f))

                    Column(
                        Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column { }
                            Row {

                                IconButton(
                                    modifier = Modifier,
                                    onClick = { backStack.add(Screen.AddHabitPageRoute(habit.id)) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "")
                                }
//                                Box(
//                                    Modifier.border(shape = MaterialShapes.Clover8Leaf.toShape(), width = 1.dp,color = MaterialTheme.colorScheme.error)
//                                        .clip(MaterialShapes.Clover8Leaf.toShape())
//                                        .background(
//                                            MaterialTheme.colorScheme.onError
//                                        ), contentAlignment = Alignment.Center
//                                ) {
                                IconButton(
                                    modifier = Modifier,
                                    onClick = { /*viewModel.deleteHabitById(habit.id)*/
                                        showDialog.value = true
                                    }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error
                                    )
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//private fun reorderableRow() {
//    Row(modifier = with(reorderableListScope) {
//        Modifier
//            .longPressDraggableHandle()
//            .fillMaxWidth()
//    }, horizontalArrangement = Arrangement.SpaceBetween)
//    {}
//}