package com.zavedahmad.yaHabit.hilt

import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.HabitRepositoryImpl
import org.koin.dsl.module


val habitRepositoryModule = module {
    single<HabitRepository> { HabitRepositoryImpl(get(), get(), get()) }
}