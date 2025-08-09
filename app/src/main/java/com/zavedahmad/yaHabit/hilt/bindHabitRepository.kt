package com.zavedahmad.yaHabit.hilt

import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.HabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class bindHabitRepository {
    @Binds
    @Singleton
    abstract fun bindHabitRepository(habitRepositoryImpl: HabitRepositoryImpl): HabitRepository
}