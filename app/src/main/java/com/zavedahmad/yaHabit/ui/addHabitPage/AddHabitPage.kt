package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import com.zavedahmad.yaHabit.ui.components.MyTopABCommon
import com.zavedahmad.yaHabit.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddHabitPage(viewModel: AddHabitPageViewModel, backStack: NavBackStack) {

    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val themeReal = theme
    val existingHabitData = viewModel.existingHabitData.collectAsStateWithLifecycle().value

    if (themeReal == null || existingHabitData == null && viewModel.navKey.habitId != null) {

        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        )

    } else {
        var title  by rememberSaveable {  mutableStateOf("Add Habit")}


        val name by viewModel.habitName.collectAsStateWithLifecycle()
        val description by viewModel.habitDescription.collectAsStateWithLifecycle()

        val isNameError = remember { derivedStateOf { name.isEmpty() } }
        val setColor = viewModel.selectedColor.collectAsStateWithLifecycle()

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


        val frequencySelectorError = rememberSaveable { mutableStateOf(false) }

        val errorCommon = remember {
            derivedStateOf {
                !(!isNameError.value && !frequencySelectorError.value)

            }
        }

        LaunchedEffect(Unit){
        if (viewModel.navKey.habitId != null) {
            title = "Edit Habit"
        }}


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
                        .padding(horizontal = 20.dp)
                        .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars)),
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





                    Spacer(Modifier.height(20.dp))

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
                    Text("Frequency", fontSize = 20.sp)
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider()
                    FrequencySelector(
                        viewModel,
                        onErrorValueChange = { it ->
                            frequencySelectorError.value = it
                        })



                    Spacer(Modifier.height(30.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Color", fontSize = 20.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(20.dp))
                    ColorSelector(viewModel)
                    Spacer(Modifier.height(30.dp))
                }

            }
        }
    }
}

