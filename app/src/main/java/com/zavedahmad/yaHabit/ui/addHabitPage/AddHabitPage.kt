package com.zavedahmad.yaHabit.ui.addHabitPage

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import com.zavedahmad.yaHabit.ui.components.MyTopABCommon
import com.zavedahmad.yaHabit.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddHabitPage(viewModel: AddHabitPageViewModel, backStack: NavBackStack) {
    var title = "Add Habit"

    if (viewModel.navKey.habitId != null) {
        title = "Edit Habit"
    }

    val name by viewModel.habitName.collectAsStateWithLifecycle()
    val description by viewModel.habitDescription.collectAsStateWithLifecycle()

    val isNameError = remember { derivedStateOf { name.isEmpty() } }
    val setColor = viewModel.selectedColor.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme

    val daysRequiredForStreakCustom = rememberSaveable { mutableStateOf("3") }
    val isDaysRequiredValidCustom = rememberSaveable { mutableStateOf(true) }
    val daysRequiredForStreakWeek = rememberSaveable { mutableStateOf("3") }
    val daysRequiredForStreakMonth = rememberSaveable { mutableStateOf("3") }
    val streakLengthCustom = rememberSaveable { mutableStateOf("14") }
    val isStreakLengthValidCustom = rememberSaveable { mutableStateOf(true) }
    val isDaysRequiredValidMonth = rememberSaveable { mutableStateOf(true) }
    val isDaysRequiredValidWeek = rememberSaveable { mutableStateOf(true) }
    val isErrorCustom by remember { derivedStateOf { !(isDaysRequiredValidCustom.value && isStreakLengthValidCustom.value) } }
    val options = listOf("Everyday", "Weekly", "Monthly", "Custom")
    val streakChecked = rememberSaveable { mutableStateOf(0) }
    val errorCommon = remember {
        // TODO: Figure this part out. Currently it's showing error when no error is there. Need to fix it.
        derivedStateOf {
            if (!isNameError.value) {
                if (streakChecked.value == 1 && !isDaysRequiredValidWeek.value) {
                    true
                } else if (streakChecked.value == 2 && !isDaysRequiredValidMonth.value) {
                    true
                } else if (streakChecked.value == 3 && isErrorCustom) {
                    true
                } else {
                    false
                }
            } else {
                true
            }
        }
    }
    if (themeReal == null) {

        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        )

    } else {
        CustomTheme(
            theme = themeReal.value,
            primaryColor = setColor.value,
            useExistingTheme = true
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MyTopABCommon(backStack, scrollBehavior, title, actions = {

                        AnimatedVisibility(
                            visible = !errorCommon.value,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Button(
                                onClick = {


                                    viewModel.addHabit()
                                    backStack.removeLastOrNull()

                                },
                                modifier = Modifier,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) { Icon(Icons.Default.Check, contentDescription = "add Habit") }
                        }
                    }, showSettingsIcon = false)
                },
            ) { innerPadding ->


                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        placeholder = {
                            Text(
                                "Title of Habit",
                                fontStyle = FontStyle.Italic,
                                color = Color.Gray
                            )
                        },
                        onValueChange = {

                            viewModel.setHabitName(it)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(50.dp),
                        isError = isNameError.value,
                        singleLine = true
                    )

                    Spacer(Modifier.height(20.dp))
                    LaunchedEffect(streakChecked.value) {
                        if (streakChecked.value == 0) {
                            viewModel.setHabitFrequency(1)
                            viewModel.setHabitCycle(1)
                            viewModel.setHabitStreakType("everyday")
                        } else if (streakChecked.value == 1) {
                            viewModel.setHabitFrequency(
                                daysRequiredForStreakWeek.value.toIntOrNull() ?: 3
                            )
                            viewModel.setHabitCycle(7)
                            viewModel.setHabitStreakType("week")
                        } else if (streakChecked.value == 2) {
                            viewModel.setHabitFrequency(
                                daysRequiredForStreakMonth.value.toIntOrNull() ?: 3
                            )
                            viewModel.setHabitCycle(30)
                            viewModel.setHabitStreakType("month")
                        } else if (streakChecked.value == 3) {
                            viewModel.setHabitFrequency(
                                daysRequiredForStreakCustom.value.toIntOrNull() ?: 3
                            )
                            viewModel.setHabitCycle(streakLengthCustom.value.toIntOrNull() ?: 14)
                            viewModel.setHabitStreakType("custom")
                        }

                    }
                    Row {
                        options.forEachIndexed { index, item ->
                            val isChecked = streakChecked.value == index
                            ToggleButton(
                               checked = isChecked, shapes = when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes(pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape, shape = ButtonGroupDefaults.connectedLeadingButtonPressShape)
                                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes(pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,shape = ButtonGroupDefaults.connectedTrailingButtonPressShape)
                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes(pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,shape = ButtonGroupDefaults.connectedMiddleButtonPressShape)
                                }, onCheckedChange = {
                                    if (!isChecked) {

                                        streakChecked.value = index
                                    }
                                })

                            {
                                Row {  /*AnimatedVisibility(visible = streakChecked.value == index) {
                                Icon(Icons.Default.Check, contentDescription = "selected", modifier = Modifier.size(15.dp))
                            }*/
                                Text(item)
                            }}
                        }
                    }

                    AnimatedVisibility(visible = streakChecked.value == 1) {
                        Column {
                            InvalidValueIndicator(!isDaysRequiredValidWeek.value)
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier.width(70.dp),
                                    value = daysRequiredForStreakWeek.value,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    onValueChange = {
                                        if (it.toIntOrNull() != null) {
                                            if (it.toInt() < 7 && it.toInt() > 0) {
                                                daysRequiredForStreakWeek.value = it
                                                isDaysRequiredValidWeek.value = true
                                                viewModel.setHabitFrequency(it.toInt())
                                            } else if (it.toIntOrNull() == 0) {
                                                daysRequiredForStreakWeek.value = it
                                                isDaysRequiredValidWeek.value = false

                                            }


                                        } else {
                                            daysRequiredForStreakWeek.value = ""
                                            isDaysRequiredValidWeek.value = false
                                        }
                                    })
                                Spacer(Modifier.width(10.dp))
                                Text("times per Week")

                            }
                        }
                    }
                    AnimatedVisibility(visible = streakChecked.value == 2) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.width(70.dp),
                                value = daysRequiredForStreakMonth.value,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                onValueChange = {
                                    if (it.toIntOrNull() != null) {
                                        if (it.toInt() < 30 && it.toInt() > 0) {
                                            daysRequiredForStreakMonth.value = it
                                            isDaysRequiredValidMonth.value = true
                                            viewModel.setHabitFrequency(it.toInt())
                                        } else if (it.toIntOrNull() == 0) {
                                            daysRequiredForStreakMonth.value = it
                                            isDaysRequiredValidMonth.value = false

                                        }
                                    } else {
                                        daysRequiredForStreakMonth.value = ""
                                        isDaysRequiredValidMonth.value = false
                                    }
                                })
                            Spacer(Modifier.width(10.dp))
                            Text("times per Month")

                        }
                    }
                    AnimatedVisibility(visible = streakChecked.value == 3) {
                        Column {

                                InvalidValueIndicator(isErrorCustom)

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier.width(70.dp),
                                    value =
                                        daysRequiredForStreakCustom.value,


                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ), onValueChange = {
                                        val num = it.toIntOrNull()
                                        if (num != null) {
                                            if (num < 999) {
                                                if (streakLengthCustom.value.isNotEmpty() && num >= streakLengthCustom.value.toInt()) {
                                                    daysRequiredForStreakCustom.value = it
                                                    isDaysRequiredValidCustom.value = false
                                                    viewModel.setHabitFrequency(it.toInt())
                                                    isStreakLengthValidCustom.value = true
                                                } else {
                                                    isDaysRequiredValidCustom.value = true
                                                    daysRequiredForStreakCustom.value =
                                                        num.toString()
                                                    viewModel.setHabitFrequency(it.toInt())
                                                }
                                            }

                                        } else {
                                            daysRequiredForStreakCustom.value = ""
                                            isDaysRequiredValidCustom.value = false
                                        }
                                    },
                                    keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("times per")
                                Spacer(Modifier.width(10.dp))
                                TextField(
                                    modifier = Modifier.width(70.dp),
                                    value = streakLengthCustom.value,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ), onValueChange = {

                                        val num = it.toIntOrNull()
                                        if (num != null) {
                                            if (num <= 999 && num > 0) {
                                                if (daysRequiredForStreakCustom.value.isNotEmpty() && num > daysRequiredForStreakCustom.value.toInt()) {
                                                    streakLengthCustom.value = it
                                                    isStreakLengthValidCustom.value = true
                                                    viewModel.setHabitCycle(it.toInt())
                                                    isDaysRequiredValidCustom.value = true
                                                } else {
                                                    isStreakLengthValidCustom.value = false
                                                    streakLengthCustom.value = it
                                                    viewModel.setHabitFrequency(it.toInt())
                                                }
                                            }

                                        } else {
                                            streakLengthCustom.value = ""
                                            isStreakLengthValidCustom.value = false
                                        }

                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("days")

                                /*AnimatedVisibility(visible = isErrorCustom) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }*/
                               /* AnimatedVisibility(visible = !isErrorCustom) {
                                    Icon(Icons.Default.Check, contentDescription = "")
                                }*/
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    TextField(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        value = description,
                        placeholder = {
                            Text(
                                "Description",
                                fontStyle = FontStyle.Italic,
                                color = Color.Gray
                            )
                        },
                        onValueChange = { viewModel.setHabitDescription(it) },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(20.dp),


                        )
                    Spacer(Modifier.height(20.dp))



                    Spacer(Modifier.height(30.dp))

                    Spacer(Modifier.height(30.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose Color", fontSize = 20.sp)
                    }
                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(20.dp))
                    ColorSelector(viewModel)
                }

            }
        }
    }
}

@Composable
fun InvalidValueIndicator(isVisible: Boolean) {
    AnimatedVisibility( modifier = Modifier.fillMaxWidth(), visible = isVisible) {
        Box(Modifier.clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.error)){
        Text("Invalid Values" , Modifier.padding(10.dp), color = MaterialTheme.colorScheme.onError)
        }
    }

}