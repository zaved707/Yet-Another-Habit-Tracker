package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.ui.components.MyMediumTopABCommon
import com.zavedahmad.yaHabit.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPage(backStack: SnapshotStateList<NavKey>, viewModel: MainPageViewModel) {
    val habits = viewModel.habits.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())



    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { MyMediumTopABCommon(backStack, scrollBehavior, "Habits") },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { backStack.add(Screen.AddHabitPageRoute) }) {
                Text("Add Habit")
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {


            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                items(habits.value.size) { index ->
                    val habit = habits.value[index]
                    CustomTheme(theme = "system", primaryColor = habit.color) {
                       HabitItem(backStack,viewModel,habit)

                        Spacer(Modifier.height(40.dp))
                        if (index == habits.value.size-1){
                            Spacer(Modifier.height(40.dp))
                        }


                    }
                }


            }
        }
    }
}

