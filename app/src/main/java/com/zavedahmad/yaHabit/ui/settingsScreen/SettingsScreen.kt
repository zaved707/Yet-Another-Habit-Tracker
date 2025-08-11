package com.zavedahmad.yaHabit.ui.settingsScreen

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InvertColors
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.components.CardMyStyle

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun SettingsScreen(
    backStack: SnapshotStateList<NavKey>,
    viewModel: SettingsViewModel,
    onDatabaseImport: (Boolean) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var isThemeDialogActive by remember { mutableStateOf(false) }
    val dynamicColor = viewModel.dynamicColor.collectAsStateWithLifecycle().value
    val firstDayOfWeek = viewModel.firstDayOfWeek.collectAsStateWithLifecycle().value
    val themeNow by viewModel.themeMode.collectAsStateWithLifecycle()
    val amoledColors = viewModel.amoledTheme.collectAsStateWithLifecycle().value
    val dialogueSelectWeekDayOpen = rememberSaveable { mutableStateOf(false) }
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
                CardMyStyle(

                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(300.dp)

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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)

        ) {
            SettingsHeading("THEMING", topPadding = false)
            SettingsItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.DarkMode,
                        contentDescription = "Info"
                    )
                },
                title = "Theme Mode",
                description = themeNow?.value ?: "system",
                task = { isThemeDialogActive = true })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                SettingsItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Info"
                        )
                    },
                    title = "Dynamic Color",

                    task = { viewModel.setDynamicColor((!dynamicColor?.value.toBoolean()).toString()) },
                    actions = {
                        Row {
//                        VerticalDivider()
                            Spacer(Modifier.width(20.dp))
                            Switch(
                                checked = if (dynamicColor?.value == null) {
                                    false
                                } else {
                                    dynamicColor.value == "true"
                                }, onCheckedChange = { viewModel.setDynamicColor(it.toString()) })
                        }
                    })
            }

            SettingsItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.InvertColors,
                        contentDescription = "Info"
                    )
                },
                title = "Amoled",
                description = "use Amoled Colors (only for dark theme).",
                task = { viewModel.setAmoledTheme((!amoledColors?.value.toBoolean()).toString()) },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // VerticalDivider()
                        Spacer(Modifier.width(20.dp))
                        Switch(
                            checked = if (amoledColors?.value == null) {
                                false
                            } else {
                                amoledColors.value == "true"
                            }, onCheckedChange = { viewModel.setAmoledTheme(it.toString()) })
                    }
                })

            SettingsHeading("DISPLAY")
            SettingsItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Info"
                    )
                },
                title = "Select First Day Of Week",
                description = firstDayOfWeek?.name,
                task = { dialogueSelectWeekDayOpen.value = true },
            )
            if (firstDayOfWeek != null) {
                ModalForWeekSelection(
                    dialogueSelectWeekDayOpen.value,
                    onDismissRequest = { dialogueSelectWeekDayOpen.value = false },
                    currentlySelectedDay = firstDayOfWeek,
                    onDaySelect = { dayOfWeek ->
                        viewModel.setFirstWeekOfDay(dayOfWeek)
                        dialogueSelectWeekDayOpen.value = false
                    }
                )
            }
            SettingsHeading("DATA")
            ExportDatabaseSettingsItem(viewModel)
            ImportDatabaseSettingsItem(viewModel, onDatabaseImport = onDatabaseImport)
            SettingsHeading("MISC")
            SettingsItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info"
                    )
                },
                title = "About",
                task = { backStack.add(Screen.AboutPageRoute) }
            )
        }


    }
}

