package com.zavedahmad.yaHabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.zavedahmad.yaHabit.ui.calenderPage.CalenderPage
import com.zavedahmad.yaHabit.ui.calenderPage.CalendarPageViewModel
import com.zavedahmad.yaHabit.ui.errorPages.SplashScreen
import com.zavedahmad.yaHabit.ui.habitsDetailPage.HabitDetailsPage
import com.zavedahmad.yaHabit.ui.habitsDetailPage.HabitDetailsPageViewModel


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
    data class CalenderPageRoute(val month: String, val habitId: Int) : Screen()

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
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setEdgeToEdgeConfig()
        setContent {

            val backStack = rememberNavBackStack<Screen>(Screen.TestingPageRoute)

            val viewModelMainPage = hiltViewModel<MainPageViewModel>()


            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val theme by settingsViewModel.themeMode.collectAsStateWithLifecycle()
            val isDynamicColor by settingsViewModel.dynamicColor.collectAsStateWithLifecycle()
            val isAmoledColor by settingsViewModel.amoledTheme.collectAsStateWithLifecycle()
            val themeReal = theme
            val realDynamicColors = isDynamicColor
            val firstDayOfWeek =
                settingsViewModel.firstDayOfWeek.collectAsStateWithLifecycle().value
            val isReady =
                !(themeReal == null || realDynamicColors == null || isAmoledColor == null || firstDayOfWeek == null)
            splashScreen.setKeepOnScreenCondition { !isReady }
            if (isReady)

            /* Box(
                 Modifier
                     .fillMaxSize()
                     .background(MaterialTheme.colorScheme.surface)
             )*/
//                    SplashScreen()
            {

                ComposeTemplateTheme(
                    themeReal.value,
                    dynamicColor = realDynamicColors.value == "true"
                ) {
                    CustomTheme(
                        theme = themeReal.value,
                        useExistingTheme = true,
                        isAmoled = isAmoledColor?.value == "true"
                    ) {

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

                                                    MainPageReorderable(
                                                        backStack,
                                                        viewModelMainPage
                                                    )
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

                                        is Screen.CalenderPageRoute -> {
                                            NavEntry(key = key) {
                                                val calendarPageViewModel =
                                                    hiltViewModel<CalendarPageViewModel, CalendarPageViewModel.Factory>(
                                                        creationCallback = { factory ->
                                                            factory.create(key)
                                                        }
                                                    )
                                                CalenderPage(calendarPageViewModel)
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
                                                HabitDetailsPage(
                                                    habitDetailsPageViewModel,
                                                    backStack
                                                )


                                            }
                                        }

                                        is Screen.TestingPageRoute -> {
                                            NavEntry(key = key) {
                                                TestingPage(backStack)
                                            }
                                        }


                                        else -> throw RuntimeException("Invalid NavKey.")
                                    }


                                },
                                transitionSpec = {
                                    slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(
                                        targetOffsetX = { -it / 2 })
                                }, popTransitionSpec = {
                                    slideInHorizontally(initialOffsetX = { -it / 2 }) togetherWith slideOutHorizontally(
                                        targetOffsetX = { it })
                                },
                                predictivePopTransitionSpec = {
                                    slideInHorizontally(initialOffsetX = { -it / 2 }) togetherWith slideOutHorizontally(
                                        targetOffsetX = { it })
                                }
                            )

                        }
                    }
                }

            }
        }
    }


}