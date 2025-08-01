package com.zavedahmad.yaHabit.ui.mainPage


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionDao
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitDao
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitRepository
import com.zavedahmad.yaHabit.roomDatabase.MainDatabase
import com.zavedahmad.yaHabit.roomDatabase.PreferenceEntity
import com.zavedahmad.yaHabit.roomDatabase.PreferencesDao
import com.zavedahmad.yaHabit.roomDatabase.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.time.DayOfWeek
import java.time.LocalDate

data class HabitWithEntries(
    val habit: HabitEntity,
    val entries: List<HabitCompletionEntity>?
)

@HiltViewModel()
class MainPageViewModel @Inject constructor(
    val habitCompletionDao: HabitCompletionDao,
    val habitDao: HabitDao,
    val preferencesDao: PreferencesDao,
    val habitRepository: HabitRepository,
    val preferencesRepository: PreferencesRepository,
    val mainDatabase: MainDatabase
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
    private val _isReorderableMode = MutableStateFlow(false)
    val isReorderableMode = _isReorderableMode.asStateFlow()
    private val _themeMode = MutableStateFlow<PreferenceEntity?>(null)
    val themeMode = _themeMode.asStateFlow()
    private val _firstDayOfWeek = MutableStateFlow<DayOfWeek?>(null)
    val firstDayOfWeek = _firstDayOfWeek.asStateFlow()
    private val _amoledTheme = MutableStateFlow<PreferenceEntity?>(null)
    val amoledTheme = _amoledTheme.asStateFlow()
    private val _devMode = MutableStateFlow<Boolean>(false)
    val devMode = _devMode.asStateFlow()
    init {
        collectAmoledTheme()
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabitsFlowSortedByIndex().collect { it -> _habits.value = it }
        }
        collectFirstDayOfWeek()
        collectThemeMode()
    }
    fun changeReorderableMode(value : Boolean){
        _isReorderableMode.value = value
    }
    fun setFirstWeekOfDay(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            preferencesRepository.setFirstDayOfWeek(dayOfWeek)
        }
    }
    fun deleteAllHabits(){
        viewModelScope.launch (Dispatchers.IO){
        habitRepository.deleteAllHabits()}
    }
    fun collectFirstDayOfWeek(){
        viewModelScope.launch { preferencesRepository.getFirstDayOfWeekFlow().collect { _firstDayOfWeek.value = it } }

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
        return habitCompletionDao.getHabitCompletionsByIdFlow(id)
    }

    fun getHabitCompletionsByDate(date: Long): Flow<List<HabitCompletionEntity>> {
        return habitCompletionDao.getHabitCompletionsByDate(date)
    }

    fun deleteEntryByDateAndHabitId(habitId: Int, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            habitCompletionDao.deleteHabitCompletionEntry(habitId = habitId, completionDate = date)
        }

    }

    fun addSampleHabits(){
        viewModelScope.launch(Dispatchers.IO) {
        habitRepository.addSampleHabits()}
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

    fun collectAmoledTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesDao.getPreferenceFlow("AmoledTheme").collect { preference ->
                _amoledTheme.value = preference ?: PreferenceEntity("AmoledTheme", "false")
            }
        }
    }


    fun move(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.move(from, to)
        }
    }
    fun exportDatabase(context: Context, uri: Uri, onComplete: (Result<Unit>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mainDatabase.close() // Close to avoid conflicts
                val dbFile = context.getDatabasePath("main_database")
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    FileInputStream(dbFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: throw IllegalStateException("Failed to open output stream")
                onComplete(Result.success(Unit))
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }

}