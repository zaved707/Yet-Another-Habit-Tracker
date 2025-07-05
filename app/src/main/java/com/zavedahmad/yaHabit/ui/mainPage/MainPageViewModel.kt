package com.zavedahmad.yaHabit.ui.mainPage


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionDao
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class habitWithEntries(
    val habit: HabitEntity,
    val entries: List<HabitCompletionEntity>?
)

@HiltViewModel()
class MainPageViewModel @Inject constructor(
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao,
    val habitRepository: HabitRepository
) :
    ViewModel() {
    override fun onCleared() {
        println("mainViewModelCleared")
    }

    private val _listStartDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val listStartDate = _listStartDate.asStateFlow()
    private val _listEndDate = MutableStateFlow<LocalDate>(LocalDate.now().minusDays(30))
    val listEndDate = _listEndDate.asStateFlow()

    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits

    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabitsFlowSortedByIndex().collect { it -> _habits.value = it }
        }
        collectThemeMode()
    }

    fun moveHabits(from: Int, to: Int) {
        _habits.value = _habits.value.toMutableList().apply { add(to, removeAt(from)) }

    }

    fun addHabitEntry(habitId: Int, completionDate: LocalDate) {
        viewModelScope.launch {
            habitCompletionDao.addHabitCompletionEntry(
                HabitCompletionEntity(
                    habitId = habitId,
                    completionDate = completionDate
                )
            )
        }
    }

    fun getHabitCompletionsByHabitId(id: Int): Flow<List<HabitCompletionEntity>?> {
        return habitCompletionDao.getHabitCompletionsById(id)
    }

    fun getHabitCompletionsByDate(date: Long): Flow<List<HabitCompletionEntity>> {
        return habitCompletionDao.getHabitCompletionsByDate(date)
    }

    fun deleteEntryByDateAndHabitId(habitId: Int, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            habitCompletionDao.deleteHabitCompletionEntry(habitId = habitId, completionDate = date)
        }

    }

    fun deleteHabitEntryWithPartialCheck(date: LocalDate, habitId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.deleteWithPartialCheck(
                HabitCompletionEntity(habitId, date)
            )
        }
    }

    fun generateInitialDates(): List<LocalDate> {
        val today = LocalDate.now()
        return (0L..14L).map { today.minusDays(it) }
    }

    fun generateMoreDates(lastDate: LocalDate): List<LocalDate> {
        return (1L..14L).map { lastDate.minusDays(it) }
    }

    fun deleteHabitById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { habitRepository.deleteHabit(id) }
    }

    fun collectThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("ThemeMode").collect { preference ->
                _themeMode.value = preference ?: PreferenceEntity("ThemeMode", "system")
            }
        }
    }

    fun move(from: Int, to: Int) {
        viewModelScope.launch {
            habitRepository.move(from, to)
        }
    }

}