package com.zavedahmad.yaHabit.widgets

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.database.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

@HiltViewModel()
class WidgetViewModel @Inject constructor(
    val habitRepository: HabitRepository,
    val preferencesRepository: PreferencesRepository,
    @ApplicationContext val context: Context,

    ) :
    ViewModel() {
        init {
            println("hello widget ViewModel Created")
        }

    }