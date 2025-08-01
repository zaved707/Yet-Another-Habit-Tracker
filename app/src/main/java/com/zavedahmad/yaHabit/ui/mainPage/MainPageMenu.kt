package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.theme.LocalOutlineSizes

@Composable
fun MainPageMenu(viewModel: MainPageViewModel, backStack: SnapshotStateList<NavKey>) {
    val isReorderableMode = viewModel.isReorderableMode.collectAsStateWithLifecycle()
    val menuVisible = rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { menuVisible.value = !menuVisible.value }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "More"
        )
    }
    DropdownMenu(
        modifier = Modifier.Companion.border(
            width = LocalOutlineSizes.current.small,
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.outline
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        expanded = menuVisible.value, // Set to true to show the menu
        onDismissRequest = { menuVisible.value = false }
    ) {
        Column {
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
            if (viewModel.devMode.value){
            DropdownMenuItem(onClick = {viewModel.addSampleHabits()}, text = { Text("add sample habits")})
            DropdownMenuItem(onClick = {viewModel.deleteAllHabits()}, text = {Text("clean Habits")})}
            ExportDatabase(viewModel)
        }
    }
}