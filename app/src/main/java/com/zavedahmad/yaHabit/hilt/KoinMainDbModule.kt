package com.zavedahmad.yaHabit.hilt

import android.content.Context
import androidx.room.Room
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.database.MainDatabase
import com.zavedahmad.yaHabit.database.repositories.ImportExportRepository
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import com.zavedahmad.yaHabit.ui.aboutPage.AboutPageViewModel
import com.zavedahmad.yaHabit.ui.addHabitPage.AddHabitPageViewModel
import com.zavedahmad.yaHabit.ui.habitsDetailPage.HabitDetailsPageViewModel
import com.zavedahmad.yaHabit.ui.mainPage.MainPageViewModel
import com.zavedahmad.yaHabit.ui.settingsScreen.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainDBModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = MainDatabase::class.java,
            name = "main_database"
        ).build()
    }

    single { get<MainDatabase>().preferencesDao() }
    single { get<MainDatabase>().habitDao() }
    single { get<MainDatabase>().habitCompletionDao() }
    single { PreferencesRepository(get(), get()) }
    single { ImportExportRepository(
        habitDao = get(),
        context = androidContext(),
        databaseUtils = get()
    ) }
    viewModel { MainPageViewModel(get(), get()) }

    viewModel { (navKey: Screen.AddHabitPageRoute) ->
        AddHabitPageViewModel(
            navKey = navKey,
            habitDao = get(),
            habitRepositoryImpl = get(),
            preferencesDao = get(),
            preferencesRepository = get()
        )
    }
    viewModel { (navKey: Screen.HabitDetailsPageRoute) ->
        HabitDetailsPageViewModel(
            navKey = navKey,
            habitCompletionDao = get(),
            habitDao = get(),
            preferencesDao = get(),
            habitRepository = get(),
            preferencesRepository = get()
        )
    }
    viewModel {
        SettingsViewModel(
            preferencesDao = get(),
            preferencesRepository = get(),
            importExportRepository = get()
        )
    }
    viewModel {
        AboutPageViewModel(
            preferencesRepository = get()
        )
    }

}