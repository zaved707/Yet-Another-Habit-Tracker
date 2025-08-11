package com.zavedahmad.yaHabit.ui.mainPage.HabitItemReorderable

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.R
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel

@Composable
fun HabitActions(
    habit: HabitEntity,
    viewModel: MainPageViewModel,
    backStack: SnapshotStateList<NavKey>,
    showDialog: MutableState<Boolean>
) {
    Row {
        if (habit.isArchived) {
            IconButton(
                modifier = Modifier.Companion,
                onClick = {

                    viewModel.unArchive(habit.id)

                }) {
                Icon(painterResource(R.drawable.archive_off_outline), contentDescription = "")

            }
        } else {
            IconButton(
                modifier = Modifier.Companion,
                onClick = {

                    viewModel.archive(habit.id)

                }) {
                Icon(painterResource(R.drawable.archive_outline), contentDescription = "")
            }
        }

        IconButton(
            modifier = Modifier.Companion,
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
            modifier = Modifier.Companion,
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