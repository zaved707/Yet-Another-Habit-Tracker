package com.zavedahmad.yaHabit.ui.mainPage.habitItemReorderable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.R
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitActionsDialogue(
    habit: HabitEntity,
    viewModel: MainPageViewModel,
    backStack: SnapshotStateList<NavKey>,
    showDialog: MutableState<Boolean>
) {
    val isDialogueVisible = rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { isDialogueVisible.value = !isDialogueVisible.value }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Actions"
        )
    }
    if (isDialogueVisible.value) {
        ModalBottomSheet(onDismissRequest = { isDialogueVisible.value = false }, dragHandle = {  }) {
            Box(
                Modifier
                    .padding(
                        20.dp
                    )
                    .padding(bottom = 10.dp)
            ) {
                Items(
                    habit,
                    viewModel,
                    backStack,
                    showDialog,
                    closeBottomSheet = { isDialogueVisible.value = false })

            }
        }
    }
}

@Composable
private fun Items(
    habit: HabitEntity,
    viewModel: MainPageViewModel,
    backStack: SnapshotStateList<NavKey>,
    showDialog: MutableState<Boolean>,
    closeBottomSheet: () -> Unit
) {

    Row {
        if (habit.isArchived) {
            HabitActionItem(icon = {
                Icon(
                    painterResource(R.drawable.archive_off_outline),
                    contentDescription = ""
                )
            }, label = "Unarchive", clickAction = {
                closeBottomSheet()

                viewModel.unArchive(habit.id)
            })
        } else {
            HabitActionItem(icon = {
                Icon(
                    painterResource(R.drawable.archive_outline),
                    contentDescription = ""
                )
            }, label = "Archive", clickAction = {
                closeBottomSheet()

                viewModel.archive(habit.id)
            })
        }

        HabitActionItem(icon = {
            Icon(Icons.Default.Edit, contentDescription = "")
        }, label = "Edit", clickAction = {
            closeBottomSheet()
            backStack.add(Screen.AddHabitPageRoute(habit.id))
        })

        HabitActionItem(icon = {
            Icon(
                Icons.Default.Delete,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error
            )
        }, label = "Delete", clickAction = {
            showDialog.value = true
        })

    }
}

@Composable
fun HabitActionItem(
    icon: @Composable () -> Unit,
    label: String,
    clickAction: () -> Unit
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = { clickAction() })
            .padding(8.dp)
    )
    {
        Column(
            Modifier
                .width(50.dp).height(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis// Optional: centers the text horizontally
            )
        }
    }
}