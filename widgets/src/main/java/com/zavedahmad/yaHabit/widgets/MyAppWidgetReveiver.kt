package com.zavedahmad.yaHabit.widgets

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class MyAppWidgetReceiver : GlanceAppWidgetReceiver(){
    override val glanceAppWidget: MyAppWidget
        get() = MyAppWidget()

}