package com.zavedahmad.yaHabit.hilt

import androidx.room.Room
import com.zavedahmad.yaHabit.database.MainDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainDBModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = MainDatabase::class.java,
            name = "main_database"
        )
    }
    single{ get<MainDatabase>().preferencesDao() }
    single { get<MainDatabase>().habitDao() }
    single { get<MainDatabase>().habitCompletionDao() }

}