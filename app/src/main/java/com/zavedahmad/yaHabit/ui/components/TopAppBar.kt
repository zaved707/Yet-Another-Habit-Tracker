package com.zavedahmad.yaHabit.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopABCommon(
    backStack: SnapshotStateList<NavKey>,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    actions: @Composable () -> Unit = {},
    showSettingsIcon: Boolean = true
) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            Row {
                actions()
                if (showSettingsIcon) {
                    IconButton(onClick = { backStack.add(Screen.SettingsPageRoute) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}