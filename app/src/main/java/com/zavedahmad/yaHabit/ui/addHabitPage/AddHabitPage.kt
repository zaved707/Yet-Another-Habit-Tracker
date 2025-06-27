package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import com.zavedahmad.yaHabit.ui.components.MyTopABCommon
import com.zavedahmad.yaHabit.ui.searchScreen.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitPage(viewModel: AddHabitPageViewModel, backStack: NavBackStack) {
    val name by viewModel.habitName.collectAsStateWithLifecycle()
    val description by viewModel.habitDescription.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { MyTopABCommon(backStack, scrollBehavior, "add Habit") },
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalArrangement = Arrangement.Center
                ) { Button(onClick = { viewModel.addHabit() }, modifier = Modifier.fillMaxWidth()) { Text("add") } }
            }
        }) { innerPadding ->




        Column(
            modifier = Modifier
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
                onValueChange = { viewModel.setHabitName(it) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50.dp),

                singleLine = true
            )

            Spacer(Modifier.height(20.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = description,
                placeholder = {
                    Text(
                        "Title of Habit",
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
            ColorSelector(viewModel)


        }

    }
}