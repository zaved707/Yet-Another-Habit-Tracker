package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RepetitionPerDaySelector(viewModel: AddHabitPageViewModel) {
    val existingHabitData = viewModel.existingHabitData.collectAsStateWithLifecycle().value
    val customValue = remember { mutableStateOf("") }
    val upperValue = 100000.0
    val isCustomValid = remember {
        derivedStateOf {
            if (customValue.value != "") {
                val doubleValue = customValue.value.toDoubleOrNull()
                if (doubleValue != null) {
                    if (doubleValue > 0.0) {
                        true
                    } else {
                        false
                    }


                } else {
                    false
                }
            } else {
                true
            }

        }
    }


    val finalRepetitionPerDay = remember { mutableStateOf(1.0) }
    LaunchedEffect(customValue.value) {
        if (customValue.value != "") {
            val valueDouble = customValue.value.toDoubleOrNull()
            if (valueDouble != null) {
                finalRepetitionPerDay.value = valueDouble
            }
        } else {
            finalRepetitionPerDay.value = 1.0
        }
    }

    LaunchedEffect(finalRepetitionPerDay.value) {
        viewModel.setRepetitionPerDay(finalRepetitionPerDay.value)
    }
    LaunchedEffect(true) {
        existingHabitData?.let { customValue.value = existingHabitData.repetitionPerDay.toString() }
    }
    InvalidValueIndicator(visible = !isCustomValid.value)
//    Text(finalRepetitionPerDay.value.toString())
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = customValue.value,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),

        placeholder = { Text("Default Value is 1") },
        onValueChange = {

                val newValue = it.toDoubleOrNull()
                if (newValue != null && newValue < 100000 || it == "") {
                    customValue.value = it
                }


        },
        shape = RoundedCornerShape(20.dp),
        keyboardOptions =
            KeyboardOptions(keyboardType = KeyboardType.Number)
    )

}

