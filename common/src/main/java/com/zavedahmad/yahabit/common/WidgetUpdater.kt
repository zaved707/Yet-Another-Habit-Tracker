package com.zavedahmad.yahabit.common

import android.content.Context

interface WidgetUpdater {
    suspend fun updateWidgets()

}