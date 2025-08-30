package com.zavedahmad.yaHabit.widgets.overviewWidget

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import com.zavedahmad.yaHabit.database.daos.HabitDao
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yahabit.common.WidgetUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class HabitWidgetRepository( private val context: Context, val widgetUpdater: WidgetUpdater){
    suspend fun update(){
        widgetUpdater.updateWidgets()
    }

}
class MyAppWidget : GlanceAppWidget(), KoinComponent {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Scaffold {
                    val habitRepository: HabitRepository = get()
                    val coroutineScope = rememberCoroutineScope()
                    val habits = remember { mutableStateOf<List<HabitEntity>>(emptyList()) }
                    LaunchedEffect(Unit) {
                        coroutineScope.launch(Dispatchers.IO) {
                            habitRepository.getHabitsFlowSortedByIndex()
                                .collect { habits.value = it }

                        }
                    }
                    if (!habits.value.isEmpty()) {
                        LazyColumn {
                            items(items = habits.value) { habit ->
                                Button(text = habit.name, onClick = {})

                            }
                        }
                    }


                }
            }
        }
    }
}
