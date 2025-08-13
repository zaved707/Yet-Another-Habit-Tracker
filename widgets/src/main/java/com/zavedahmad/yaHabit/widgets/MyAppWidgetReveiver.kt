package com.zavedahmad.yaHabit.widgets

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class MyAppWidgetReceiver : GlanceAppWidgetReceiver(){
    @Inject lateinit var habitRepository: HabitRepository
    @Inject lateinit var preferencesRepository: PreferencesRepository
    @Inject @ApplicationContext
    lateinit var context: Context
    override val glanceAppWidget: MyAppWidget
        get() = MyAppWidget(habitRepository, preferencesRepository, context)

}