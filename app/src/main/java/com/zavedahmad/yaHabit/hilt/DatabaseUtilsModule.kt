package com.zavedahmad.yaHabit.hilt

import com.zavedahmad.yaHabit.database.utils.DatabaseUtils
import com.zavedahmad.yaHabit.database.utils.LocalDatabaseUtils

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseUtilsModule = module {
    single<DatabaseUtils> { LocalDatabaseUtils(androidContext(), get()) }
}
