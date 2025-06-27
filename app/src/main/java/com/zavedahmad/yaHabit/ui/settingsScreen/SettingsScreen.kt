package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun SettingsScreen(backStack: SnapshotStateList<NavKey>, viewModel: SettingsViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var isThemeDialogActive by remember { mutableStateOf(false) }

    val themeNow by viewModel.themeMode.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack, contentDescription = "go back"
                        )
                    }
                },
                title = { Text("Settings") },

                scrollBehavior = scrollBehavior
            )

        }) { innerPadding ->

        if (isThemeDialogActive) {
            Dialog(onDismissRequest = { isThemeDialogActive = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(300.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            "Theme Mode",
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("system")

                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "system"
                            )
                            Text("Follow System")
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("dark")
                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "dark"
                            )
                            Text("Dark")
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("light")
                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "light"
                            )
                            Text("Light")
                        }
                    }
                }

            }
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)

        ) {

            SettingsItem(
                Icons.Default.DarkMode,
                title = "Theme Mode",
                description = themeNow?.value ?: "system",
                task = { isThemeDialogActive = true })

        }


    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String?, task: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = { task() })
            .fillMaxWidth()
            .padding(20.dp),

        ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            imageVector = icon,
            contentDescription = description
        )
        Spacer(Modifier.width(20.dp))
        Column {
            Text(
                title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            description?.let {
                Text(
                    description,
                    style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

