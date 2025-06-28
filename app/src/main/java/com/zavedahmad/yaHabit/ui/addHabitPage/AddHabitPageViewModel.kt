package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.Screen
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = AddHabitPageViewModel.Factory::class)
class AddHabitPageViewModel @AssistedInject constructor(
    @Assisted val navKey: Screen.AddHabitPageRoute,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(navKey: Screen.AddHabitPageRoute): AddHabitPageViewModel
    }

    val colors = listOf<Color>(


        Color(0xFFBA55D3), // Vibrant Orchid (MediumOrchid)

        Color(0xFF4682B4), // Vibrant Steel Blue (SteelBlue)

        Color(0xFF40E0D0), // Vibrant Turquoise (Turquoise)

        Color(0xFF32CD32), // Vibrant Lime Green (LimeGreen)

        Color(0xFFADFF2F), // Vibrant Green Yellow (GreenYellow)

        Color(0xFFFFD700), // Vibrant Gold (Gold)

        Color(0xFFFF8C00), // Vibrant Dark Orange (DarkOrange)

        Color(0xFFDC143C), // Crimson
        Color(0xFFA0522D), // Vibrant Sienna (Sienna)

        Color(0xFF708090),  // Vibrant Slate Gray (SlateGray)


    )

    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()
    private val _selectedColor = MutableStateFlow<Color>(colors[0])
    val selectedColor = _selectedColor.asStateFlow()
    private val _habitName = MutableStateFlow("")
    val habitName = _habitName.asStateFlow()
    private val _existingHabitData = mutableStateOf<HabitEntity?>(null)
    val existingHabitData = _existingHabitData
    private val _habitDescription = MutableStateFlow("")
    val habitDescription = _habitDescription.asStateFlow()

    init {
        collectThemeMode()
        getHabitDetails()
    }

    fun setColor(color: Color) {
        _selectedColor.value = color
    }

    fun setHabitName(name: String) {
        _habitName.value = name
    }

    fun setHabitDescription(description: String) {
        _habitDescription.value = description
    }

    fun addHabit() {
        if (navKey.habitId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                habitDao.addHabit(
                    HabitEntity(
                        id = navKey.habitId,
                        name = habitName.value,
                        color = _selectedColor.value,
                        description = _habitDescription.value
                    )
                )
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                habitDao.addHabit(
                    HabitEntity(
                        name = habitName.value,
                        color = _selectedColor.value,
                        description = _habitDescription.value
                    )
                )
            }
        }
    }

    fun collectThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("ThemeMode").collect { preference ->
                _themeMode.value = preference ?: PreferenceEntity("ThemeMode", "system")
            }
        }
    }

    fun getHabitDetails() {
        if (navKey.habitId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                _existingHabitData.value = habitDao.getHabitById(navKey.habitId)
                val existingHabitHolder = _existingHabitData.value
                if (existingHabitHolder != null) {
                    setHabitName(existingHabitHolder.name)
                    setHabitDescription(existingHabitHolder.description)
                    setColor(existingHabitHolder.color)
                }
            }
        }
    }
}