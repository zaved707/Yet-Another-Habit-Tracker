package com.zavedahmad.yaHabit.widgets.overviewWidget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.zavedahmad.yahabit.common.WidgetUpdater


class WidgetUpdaterImpl(private val context : Context) : WidgetUpdater{

    override suspend fun updateWidgets() {
        MyAppWidget().updateAll(context)
    }
}