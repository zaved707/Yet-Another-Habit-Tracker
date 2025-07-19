package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun SettingsScreen(backStack: SnapshotStateList<NavKey>, viewModel: SettingsViewModel) {
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
            modifier = Modifier.verticalScroll(rememberScrollState())
                .padding(innerPadding)

        ) {
            SettingsHeading("THEMING")
            SettingsItem(
                Icons.Default.DarkMode,
                title = "Theme Mode",
                description = themeNow?.value ?: "system",
                task = { isThemeDialogActive = true })
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Dynamic Color",

                task = {viewModel.setDynamicColor((!dynamicColor?.value.toBoolean()).toString())},
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

            SettingsItem(
                icon = Icons.Default.InvertColors,
                title = "Amoled",
                description = "use Amoled Colors (only for dark theme).",
                task = {viewModel.setAmoledTheme((!amoledColors?.value.toBoolean()).toString())},
                actions = {
                    Row( verticalAlignment = Alignment.CenterVertically) {
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
                icon = Icons.Default.CalendarToday,
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

        }


    }
}

@Composable
fun ModalForWeekSelection(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onDaySelect: (DayOfWeek) -> Unit,
    currentlySelectedDay: DayOfWeek = DayOfWeek.SUNDAY
) {

    if (isVisible) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(Modifier) {
                Column(Modifier.fillMaxWidth(0.7f)) {
                    for (i in 1..7) {
                        val currentDay = DayOfWeek.of(i)
                        Card(onClick = { onDaySelect(currentDay) }) {
                            Row(
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    currentDay.toString()
                                )
                                if (currentDay == currentlySelectedDay) {
                                    Icon(Icons.Default.Check, contentDescription = "f")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String? = null,
    task: () -> Unit,
    actions: @Composable (() -> Unit)?  = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = { task() })
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            , // Use IntrinsicSize.Min to allow flexible height
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Center items vertically

    ) {
        Row(
            modifier =  if (actions != null){Modifier

                .fillMaxWidth(0.7f)}else{Modifier.fillMaxWidth()},
            verticalAlignment = Alignment.CenterVertically
        ) { // Added weight to allow this Row to take available space
            Icon(
                modifier = Modifier.padding(10.dp),
                imageVector = icon,
                contentDescription = description
            )
            Spacer(Modifier.width(20.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(
                    title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                description?.let {
                    Text(
                        description,
                        style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        if (actions != null) {
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) { // Added fillMaxHeight to the actions Row


                actions()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsHeading(text: String) {
    Row {
        Spacer(Modifier.width(30.dp))
        Text(
            text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLargeEmphasized
        )
    }

}