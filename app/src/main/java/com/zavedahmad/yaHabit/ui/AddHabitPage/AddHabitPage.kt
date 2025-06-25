package com.zavedahmad.yaHabit.ui.AddHabitPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.zavedahmad.yaHabit.Screen

@Composable
fun AddHabitPage(viewModel: AddHabitPageViewModel) {
    val name = rememberSaveable { mutableStateOf("") }
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(onValueChange = { name.value = it }, value = name.value)
            Button(onClick = { viewModel.addHabit(name.value) }) { Text("add") }
        }
    }
}