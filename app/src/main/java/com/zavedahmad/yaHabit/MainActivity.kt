package com.zavedahmad.yaHabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.zavedahmad.yaHabit.ui.TestingPage.TestingPage
import com.zavedahmad.yaHabit.ui.addHabitPage.AddHabitPage
import com.zavedahmad.yaHabit.ui.addHabitPage.AddHabitPageViewModel
import com.zavedahmad.yaHabit.ui.habitsDetailPage.HabitDetailsPage
import com.zavedahmad.yaHabit.ui.habitsDetailPage.HabitDetailsPageViewModel
import com.zavedahmad.yaHabit.ui.mainPage.MainPage

import com.zavedahmad.yaHabit.ui.mainPage.MainPageReorderable
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel

import com.zavedahmad.yaHabit.ui.setEdgeToEdgeConfig
import com.zavedahmad.yaHabit.ui.settingsScreen.SettingsScreen
import com.zavedahmad.yaHabit.ui.settingsScreen.SettingsViewModel
import com.zavedahmad.yaHabit.ui.theme.ComposeTemplateTheme
import com.zavedahmad.yaHabit.ui.theme.CustomTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {


    @Serializable
    data object MainPageRoute : Screen()

    @Serializable
    data object TestingPageRoute : Screen()

    @Serializable
    data class AddHabitPageRoute(val habitId: Int? = null) : Screen()

    @Serializable
    data object FavouritePageRoute : Screen()

    @Serializable
    data class HabitDetailsPageRoute(val habitId: Int) : Screen()

    @Serializable
    data object SettingsPageRoute : Screen()


}


@AndroidEntryPoint
class RecipePickerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEdgeToEdgeConfig()
        setContent {
            val backStack = rememberNavBackStack<Screen>(Screen.MainPageRoute)

            val viewModelMainPage = hiltViewModel<MainPageViewModel>()

            val isTopMainPageRoute =
                if (backStack.lastOrNull() is Screen.MainPageRoute || backStack.lastOrNull() is Screen.TestingPageRoute || backStack.lastOrNull() is Screen.FavouritePageRoute) {
                    true
                } else {
                    false
                }
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val theme by settingsViewModel.themeMode.collectAsStateWithLifecycle()
            val themeReal = theme
            if (themeReal == null) {
                ComposeTemplateTheme("system") {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }
            } else {

                ComposeTemplateTheme(themeReal.value) {
                    CustomTheme(theme = themeReal.value, useExistingTheme = true) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),

                            ) { innerPadding ->
                            var absolutePadding = PaddingValues()
                            if (isTopMainPageRoute) {
                                absolutePadding =
                                    PaddingValues(bottom = innerPadding.calculateBottomPadding())
                            }

                            Box(modifier = Modifier.padding()) {


                                NavDisplay(
                                    backStack = backStack,
                                    onBack = { backStack.removeLastOrNull() },
                                    entryDecorators = listOf(
                                        rememberSceneSetupNavEntryDecorator(),
                                        rememberSavedStateNavEntryDecorator(),
                                        rememberViewModelStoreNavEntryDecorator()
                                    ),
                                    entryProvider = { key ->
                                        when (key) {

                                            is Screen.MainPageRoute -> {
                                                NavEntry(key = key) {

                                                    Column {

                                                        MainPageReorderable(backStack, viewModelMainPage)
                                                    }
                                                }
                                            }


                                            is Screen.SettingsPageRoute -> {
                                                NavEntry(key = key) {


                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {

                                                        SettingsScreen(backStack, settingsViewModel)
                                                    }
                                                }
                                            }

                                            is Screen.AddHabitPageRoute -> {
                                                NavEntry(key = key) {
                                                    val addHabitPageViewModel =
                                                        hiltViewModel<AddHabitPageViewModel, AddHabitPageViewModel.Factory>(
                                                            creationCallback = { factory ->
                                                                factory.create(key)
                                                            }
                                                        )
                                                    AddHabitPage(addHabitPageViewModel, backStack)

                                                }
                                            }

                                            is Screen.HabitDetailsPageRoute -> {
                                                NavEntry(key = key) {
                                                    val habitDetailsPageViewModel =
                                                        hiltViewModel<HabitDetailsPageViewModel, HabitDetailsPageViewModel.Factory>(
                                                            creationCallback = { factory ->
                                                                factory.create(key)
                                                            }
                                                        )
                                                    HabitDetailsPage(habitDetailsPageViewModel)


                                                }
                                            }
                                            is Screen.TestingPageRoute -> {
                                                NavEntry(key = key){
                                                    TestingPage(backStack)
                                                }
                                            }


                                            else -> throw RuntimeException("Invalid NavKey.")
                                        }


                                    }
                                )

                            }
                        }
                    }
                }
            }
        }
    }


}