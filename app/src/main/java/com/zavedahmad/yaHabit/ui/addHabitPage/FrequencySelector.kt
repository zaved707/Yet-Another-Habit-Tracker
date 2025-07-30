package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.roomDatabase.HabitStreakType
import com.zavedahmad.yaHabit.ui.components.CardMyStyle
import com.zavedahmad.yaHabit.ui.components.DialogItem
import com.zavedahmad.yaHabit.ui.habitsDetailPage.formatHabitFrequency
import com.zavedahmad.yaHabit.ui.theme.LocalOutlineSizes
import com.zavedahmad.yaHabit.utils.formatNumber.softFormatNumber

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FrequencySelector(viewModel: AddHabitPageViewModel, onErrorValueChange: (Boolean) -> Unit) {
    val streakChecked = rememberSaveable { mutableStateOf(0) }
    val frequencyEveryDay = rememberSaveable { mutableStateOf("1") }
    val frequencyCustom = rememberSaveable { mutableStateOf("3") }
    val frequencyValidCustom =
        remember { derivedStateOf { frequencyCustom.value.toDoubleOrNull() != null } }
    val frequencyForStreakWeek = rememberSaveable { mutableStateOf("3") }
    val frequencyForStreakMonth = rememberSaveable { mutableStateOf("3") }
    val cycleLengthCustom = rememberSaveable { mutableStateOf("14") }
    val isCycleValidCustom =
        remember { derivedStateOf { cycleLengthCustom.value.toIntOrNull() != null } }
    val isFrequencyValidMonth =
        remember { derivedStateOf { frequencyForStreakMonth.value.toDoubleOrNull() != null } }
    val isFrequencyValidWeek =
        remember { derivedStateOf { frequencyForStreakWeek.value.toDoubleOrNull() != null } }
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val isErrorCustom by remember { derivedStateOf { !(frequencyValidCustom.value && isCycleValidCustom.value) } }
    val isErrorDaily =
        remember { derivedStateOf { frequencyEveryDay.value.toDoubleOrNull() == null } }
    val options = listOf("Everyday", "Weekly", "Monthly", "Custom")
    val existingHabitData = viewModel.existingHabitData.collectAsStateWithLifecycle().value
    val measurementUnit by viewModel.measurementUnit.collectAsStateWithLifecycle()
    val viewModelFrequency = viewModel.habitFrequency.collectAsStateWithLifecycle()
    val viewModelCycle = viewModel.habitCycle.collectAsStateWithLifecycle()
    val viewModelHabitStreakType = viewModel.habitStreakType.collectAsStateWithLifecycle()
    val upperLimit = 1000000
    val errorCommon = remember {

        derivedStateOf {
            if (streakChecked.value == 0 && isErrorDaily.value) {
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

                if (existingHabitData.streakType == HabitStreakType.DAILY) {
                    frequencyEveryDay.value = existingHabitData.frequency.toString()
                    streakChecked.value = 0

                } else if (existingHabitData.streakType == HabitStreakType.WEEKLY) {
                    frequencyForStreakWeek.value = existingHabitData.frequency.toString()
                    streakChecked.value = 1
                } else if (existingHabitData.streakType == HabitStreakType.MONTHLY) {
                    streakChecked.value = 2
                    frequencyForStreakMonth.value = existingHabitData.frequency.toString()
                } else if (existingHabitData.streakType == HabitStreakType.CUSTOM) {
                    streakChecked.value = 3
                    cycleLengthCustom.value = existingHabitData.cycle.toString()
                    frequencyCustom.value = existingHabitData.frequency.toString()
                }
            }
        }
    }
    val checkErrorsAndRectify = {
        // this will set the value to whatever is in viewmodel
        if (isErrorDaily.value) {
            if (streakChecked.value == 0) {
                frequencyEveryDay.value = softFormatNumber(viewModelFrequency.value ?: 0.0)
            } else {
                frequencyEveryDay.value = "1"
            }
        }
        if (!isFrequencyValidWeek.value) {
            if (streakChecked.value == 1) {
                frequencyForStreakWeek.value = softFormatNumber(viewModelFrequency.value ?: 0.0)
            } else {
                frequencyForStreakWeek.value = "3"
            }
        }
        if (!isFrequencyValidMonth.value) {
            if (streakChecked.value == 2) {
                frequencyForStreakMonth.value = softFormatNumber(viewModelFrequency.value ?: 0.0)
            } else {
                frequencyForStreakMonth.value = "3"
            }
        }
        if (isErrorCustom) {
            if (streakChecked.value == 3) {
                frequencyCustom.value = softFormatNumber(viewModelFrequency.value ?: 0.0)
                cycleLengthCustom.value = viewModelCycle.value.toString()
            } else {
                frequencyCustom.value = "3"
                cycleLengthCustom.value = "14"
            }

        }
    }   //TODO enable soft formatting of numbers whne rectified

    // this keeps track of when user selects another kind of frequency it sets appropriate values for its fields
    LaunchedEffect(streakChecked.value) {
        if (streakChecked.value == 0) {
            viewModel.setHabitFrequency(frequencyEveryDay.value.toDoubleOrNull() ?: 1.0)
            viewModel.setHabitCycle(1)
            viewModel.setHabitStreakType(HabitStreakType.DAILY)
        } else if (streakChecked.value == 1) {
            viewModel.setHabitFrequency(
                frequencyForStreakWeek.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(7)
            viewModel.setHabitStreakType(HabitStreakType.WEEKLY)
        } else if (streakChecked.value == 2) {
            viewModel.setHabitFrequency(
                frequencyForStreakMonth.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(30)
            viewModel.setHabitStreakType(HabitStreakType.MONTHLY)
        } else if (streakChecked.value == 3) {
            viewModel.setHabitFrequency(
                frequencyCustom.value.toDoubleOrNull() ?: 3.0
            )
            viewModel.setHabitCycle(cycleLengthCustom.value.toIntOrNull() ?: 14)
            viewModel.setHabitStreakType(HabitStreakType.CUSTOM)
        }

    }

    val animatedVisibilityOne = @Composable {
        if (streakChecked.value == 0) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                InvalidValueIndicator(visible = isErrorDaily.value)
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

                        }
                    },
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

                }


            }
        }
    }
    val animatedVisibilityTwo = @Composable {
        if (streakChecked.value == 1) {
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

                                    viewModel.setHabitFrequency(it.toDouble())
                                } else if (it.toDoubleOrNull() == 0.0) {
                                    frequencyForStreakWeek.value = it


                                }


                            } else {
                                frequencyForStreakWeek.value = ""

                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = { Text(measurementUnit ?: "Unit") })

                }
                Spacer(Modifier.width(10.dp))

                Text("per Week")
            }
        }
    }
    val animatedVisibilityThree = @Composable {
        if (streakChecked.value == 2) {
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

                                    viewModel.setHabitFrequency(it.toDouble())
                                } else if (it.toDoubleOrNull() == 0.0) {
                                    frequencyForStreakMonth.value = it


                                }
                            } else {
                                frequencyForStreakMonth.value = ""

                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = { Text(measurementUnit ?: "Unit") })


                }
                Spacer(Modifier.width(10.dp))

                Text("per Month")
            }
        }
    }
    val animatedVisibilityFour = @Composable {
        if (streakChecked.value == 3) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InvalidValueIndicator(
                    Modifier.fillMaxWidth(0.5f),
                    visible = isErrorCustom
                )

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

                                        viewModel.setHabitFrequency(it.toDouble())

                                    } else {

                                        frequencyCustom.value =
                                            num.toString()
                                        viewModel.setHabitFrequency(it.toDouble())
                                    }
                                }

                            } else {
                                frequencyCustom.value = ""

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

                                            viewModel.setHabitCycle(it.toInt())

                                        } else {

                                            cycleLengthCustom.value = it
                                            viewModel.setHabitCycle(it.toInt())
                                        }
                                    }

                                } else {
                                    cycleLengthCustom.value = ""

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
    val animatedVisibilities = listOf<@Composable () -> Unit>(
        animatedVisibilityOne,
        animatedVisibilityTwo,
        animatedVisibilityThree,
        animatedVisibilityFour
    )
    // These are all the buttons

    OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
        checkErrorsAndRectify()
        showDialog.value = true
    }) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                formatHabitFrequency(
                    streakType = viewModelHabitStreakType.value,
                    frequency = viewModelFrequency.value ?: 0.0,
                    cycle = viewModelCycle.value ?: 0,
                    formatFrequencyNumber = true
                )
            )
            Icon(Icons.Default.ArrowDropDown, "Show Options")
        }
    }
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            CardMyStyle(modifier = Modifier .widthIn(max = 300.dp)
                .fillMaxWidth()) {
                 Column(
                     Modifier
                         .fillMaxWidth()
                         .padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
                 ) {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.End
                     ) { Button(onClick = { showDialog.value = false }) { Text("Done") } }
                     options.forEachIndexed { index, item ->
                         val isChecked = streakChecked.value == index
                         DialogItem(
                             modifier = Modifier.fillMaxWidth(),
                             state = isChecked,
                             onValueChange = {
                                 if (!isChecked) {
                                     checkErrorsAndRectify()

                                     streakChecked.value = index
                                 }
                             }) {
                             Box(Modifier.padding(15.dp)) {
                                 Column {
                                     Row(
                                         modifier = Modifier.fillMaxWidth(),
                                         horizontalArrangement = Arrangement.SpaceBetween
                                     ) {
                                         Text(
                                             item,
                                             overflow = TextOverflow.MiddleEllipsis,
                                             maxLines = 1
                                         )

                                         RadioButton(selected = isChecked, onClick = null)

                                     }
                                     Spacer(Modifier.height(20.dp))
                                     animatedVisibilities[index]()
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
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}