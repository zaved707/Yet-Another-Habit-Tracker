package com.zavedahmad.yaHabit.widgets

import android.content.Context
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyAppWidgetRepository @Inject internal constructor(
    private val habitRepository: HabitRepository,
    @ApplicationContext private val appContext: Context
){
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetModelRepositoryEntryoint {
        fun widgetModelRepository(): MyAppWidgetRepository
    }

    companion object {
        fun get(applicationContext: Context): MyAppWidgetRepository {
            var widgetModelRepositoryEntryoint: WidgetModelRepositoryEntryoint = EntryPoints.get(
                applicationContext,
                WidgetModelRepositoryEntryoint::class.java,
            )
            return widgetModelRepositoryEntryoint.widgetModelRepository()
        }
    }
}