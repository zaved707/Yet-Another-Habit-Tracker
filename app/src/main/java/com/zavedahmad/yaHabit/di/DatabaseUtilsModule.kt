package com.zavedahmad.yaHabit.di

import com.zavedahmad.yaHabit.database.utils.DatabaseUtils
import com.zavedahmad.yaHabit.database.utils.LocalDatabaseUtils

import org.koin.dsl.module

val databaseUtilsModule = module {
    single<DatabaseUtils> { LocalDatabaseUtils(get(), get()) }
}
