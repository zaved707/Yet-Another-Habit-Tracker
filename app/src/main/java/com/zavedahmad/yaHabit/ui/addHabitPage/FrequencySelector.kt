package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FrequencySelector(viewModel: AddHabitPageViewModel, onErrorValueChange: (Boolean) -> Unit) {
    val streakChecked = rememberSaveable { mutableStateOf(0) }
    val frequencyEveryDay = rememberSaveable { mutableStateOf("1") }
    val frequencyCustom = rememberSaveable { mutableStateOf("3") }
    val frequencyValidCustom = rememberSaveable { mutableStateOf(true) }
    val frequencyForStreakWeek = rememberSaveable { mutableStateOf("3") }
    val frequencyForStreakMonth = rememberSaveable { mutableStateOf("3") }
    val cycleLengthCustom = rememberSaveable { mutableStateOf("14") }
    val isCycleValidCustom = rememberSaveable { mutableStateOf(true) }
    val isFrequencyValidMonth = rememberSaveable { mutableStateOf(true) }
    val isFrequencyValidWeek = rememberSaveable { mutableStateOf(true) }

    val isErrorCustom by remember { derivedStateOf { !(frequencyValidCustom.value && isCycleValidCustom.value) } }
    var isErrorDaily by remember { mutableStateOf(false) }
    val options = listOf("Everyday", "Weekly", "Monthly", "Custom")
    val existingHabitData = viewModel.existingHabitData.collectAsStateWithLifecycle().value
    val measurementUnit by viewModel.measurementUnit.collectAsStateWithLifecycle()

    val upperLimit = 1000000
    val errorCommon = remember {

        derivedStateOf {
            if (streakChecked.value == 0 && isErrorDaily) {
                true
            } else if (streakChecked.value == 1 && !isFrequencyValidWeek.value) {
                true
            } else if (streakChecked.value == 2 && !isFrequencyValidMonth.value) {
                true
            } else if (streakChecked.value == 3 && isErrorCustom) {
                true
            } else {
                false
            }
        }
    }
    LaunchedEffect(errorCommon.value) {
        onErrorValueChange(errorCommon.value)

    }
    // this sets the initial value if the habit already exists in database
    existingHabitData?.let {
        LaunchedEffect(Unit) {
            if (viewModel.navKey.habitId != null) {

                if (existingHabitData.streakType == "everyday") {
                    frequencyEveryDay.value = existingHabitData.frequency.toString()
                    streakChecked.value = 0

                } else if (existingHabitData.streakType == "week") {
                    frequencyForStreakWeek.value = existingHabitData.frequency.toString()
                    streakChecked.value = 1
                } else if (existingHabitData.streakType == "month") {
                    streakChecked.value = 2
                    frequencyForStreakMonth.value = existingHabitData.frequency.toString()
                } else if (existingHabitData.streakType == "custom") {
                    streakChecked.value = 3
                    cycleLengthCustom.value = existingHabitData.cycle.toString()
                    frequencyCustom.value = existingHabitData.frequency.toString()
                }
            }
        }
    }
    // this keeps track of when user selects another kind of frequency it sets appropriate values for its fields
    LaunchedEffect(streakChecked.value) {
        if (streakChecked.value == 0) {
            viewModel.setHabitFrequency(frequencyEveryDay.value.toDoubleOrNull() ?: 1.0)
            viewModel.setHabitCycle(1)
            viewModel.setHabitStreakType("everyday")
        } else if (streakChecked.value == 1) {
            viewModel.setHabitFrequency(
                frequencyForStreakWeek.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(7)
            viewModel.setHabitStreakType("week")
        } else if (streakChecked.value == 2) {
            viewModel.setHabitFrequency(
                frequencyForStreakMonth.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(30)
            viewModel.setHabitStreakType("month")
        } else if (streakChecked.value == 3) {
            viewModel.setHabitFrequency(
                frequencyCustom.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(cycleLengthCustom.value.toIntOrNull() ?: 14)
            viewModel.setHabitStreakType("custom")
        }

    }
    // These are all the buttons
    Row(Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, item ->
            val isChecked = streakChecked.value == index
            Row(
                modifier = if (!isChecked) {
                    Modifier.weight(1f)
                } else {
                    Modifier
                       .weight(1.4f)
                        .wrapContentWidth()
                }
            ) {
                ToggleButton(                                                   //TODO this shrinks on large screens fix it
                    modifier = if (!isChecked) {
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .weight(1.2f)
                    },
                    checked = isChecked, shapes = when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes(
                            pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                            shape = ButtonGroupDefaults.connectedLeadingButtonPressShape
                        )

                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes(
                            pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                            shape = ButtonGroupDefaults.connectedTrailingButtonPressShape
                        )

                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes(
                            pressedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                            shape = ButtonGroupDefaults.connectedMiddleButtonPressShape
                        )
                    }, onCheckedChange = {
                        if (!isChecked) {

                            streakChecked.value = index
                        }
                    })

                {
                    Row {  /*AnimatedVisibility(visible = streakChecked.value == index) {
                                Icon(Icons.Default.Check, contentDescription = "selected", modifier = Modifier.size(15.dp))
                            }*/
                        Text(item, overflow = TextOverflow.MiddleEllipsis, maxLines = 1)
                    }
                }
            }
        }
    }
    Card(modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { // Changed Box to Column here
            AnimatedVisibility(visible = streakChecked.value == 0) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InvalidValueIndicator(visible = isErrorDaily)
                    OutlinedTextField(
                        value = frequencyEveryDay.value,
                        onValueChange = {
                            val num = it.toDoubleOrNull()
                            if (num != null) {
                                if (num < upperLimit) {
                                   frequencyEveryDay.value = it
                                }

                            } else {
                                frequencyEveryDay.value = ""
                                isErrorDaily = false
                            } },
                        keyboardOptions =
                            KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            Text(
                                measurementUnit ?: "Unit"
                            )
                        })

                    Text("Everyday")
                }
                LaunchedEffect(frequencyEveryDay.value) {

                    val frequency = frequencyEveryDay.value.toDoubleOrNull()
                    if (frequency != null) {
                        viewModel.setHabitFrequency(frequency)
                        isErrorDaily = false
                    } else {
                        isErrorDaily = true
                    }
                }
            }


            AnimatedVisibility(visible = streakChecked.value == 1) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InvalidValueIndicator(
                        Modifier.fillMaxWidth(0.5f),
                        visible = !isFrequencyValidWeek.value
                    )


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = frequencyForStreakWeek.value,

                            onValueChange = {
                                if (it.toDoubleOrNull() != null) {
                                    if (it.toDouble() < upperLimit && it.toDouble() > 0) {
                                        frequencyForStreakWeek.value = it
                                        isFrequencyValidWeek.value = true
                                        viewModel.setHabitFrequency(it.toDouble())
                                    } else if (it.toDoubleOrNull() == 0.0) {
                                        frequencyForStreakWeek.value = it
                                        isFrequencyValidWeek.value = false

                                    }


                                } else {
                                    frequencyForStreakWeek.value = ""
                                    isFrequencyValidWeek.value = false
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            trailingIcon = { Text(measurementUnit ?: "Unit") })

                    }
                    Spacer(Modifier.width(10.dp))

                    Text("per Week")
                }
            }
            AnimatedVisibility(visible = streakChecked.value == 2) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InvalidValueIndicator(
                        Modifier.fillMaxWidth(0.5f),
                        visible = !isFrequencyValidMonth.value
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(

                            value = frequencyForStreakMonth.value,

                            onValueChange = {
                                if (it.toDoubleOrNull() != null) {
                                    if (it.toDouble() < upperLimit && it.toDouble() > 0) {
                                        frequencyForStreakMonth.value = it
                                        isFrequencyValidMonth.value = true
                                        viewModel.setHabitFrequency(it.toDouble())
                                    } else if (it.toDoubleOrNull() == 0.0) {
                                        frequencyForStreakMonth.value = it
                                        isFrequencyValidMonth.value = false

                                    }
                                } else {
                                    frequencyForStreakMonth.value = ""
                                    isFrequencyValidMonth.value = false
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            trailingIcon = { Text(measurementUnit ?: "Unit") })


                    }
                    Spacer(Modifier.width(10.dp))

                    Text("per Month")
                }
            }
            AnimatedVisibility(visible = streakChecked.value == 3) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InvalidValueIndicator(
                        Modifier.fillMaxWidth(0.5f),
                        visible = isErrorCustom
                    )
                    Text("Custom")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(

                            value =
                                frequencyCustom.value,


                            onValueChange = {
                                val num = it.toDoubleOrNull()
                                if (num != null) {
                                    if (num < upperLimit) {
                                        if (cycleLengthCustom.value.isNotEmpty()) {
                                            frequencyCustom.value = it
                                            frequencyValidCustom.value = true
                                            viewModel.setHabitFrequency(it.toDouble())
                                            isCycleValidCustom.value = true
                                        } else {
                                            frequencyValidCustom.value = true
                                            frequencyCustom.value =
                                                num.toString()
                                            viewModel.setHabitFrequency(it.toDouble())
                                        }
                                    }

                                } else {
                                    frequencyCustom.value = ""
                                    frequencyValidCustom.value = false
                                }
                            },
                            keyboardOptions =
                                KeyboardOptions(keyboardType = KeyboardType.Number),
                            trailingIcon = { Text(measurementUnit ?: "Unit") }
                        )
                        Spacer(Modifier.height(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text("per")
                            Spacer(Modifier.width(10.dp))
                            OutlinedTextField(
                                modifier = Modifier.width(70.dp),
                                value = cycleLengthCustom.value,
                                onValueChange = {

                                    val num = it.toIntOrNull()
                                    if (num != null) {
                                        if (num <= 999 && num > 0) {
                                            if (frequencyCustom.value.isNotEmpty()) {
                                                cycleLengthCustom.value = it
                                                isCycleValidCustom.value = true
                                                viewModel.setHabitCycle(it.toInt())
                                                frequencyValidCustom.value = true
                                            } else {
                                                isCycleValidCustom.value = false
                                                cycleLengthCustom.value = it
                                                viewModel.setHabitCycle(it.toInt())
                                            }
                                        }

                                    } else {
                                        cycleLengthCustom.value = ""
                                        isCycleValidCustom.value = false
                                    }

                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("days")
                        }
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
        }
    }
}

@Composable
fun InvalidValueIndicator(modifier: Modifier = Modifier, visible: Boolean) {
    AnimatedVisibility(modifier = modifier, visible = visible) {
        Box(
            Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Invalid Values",
                Modifier.padding(5.dp),
                color = MaterialTheme.colorScheme.onError,
                fontSize = 15.sp
            )
        }
    }

}