package com.zavedahmad.yaHabit.di

import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.HabitRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val habitRepositoryModule = module {
    singleOf(::HabitRepositoryImpl).bind<HabitRepository>()
}