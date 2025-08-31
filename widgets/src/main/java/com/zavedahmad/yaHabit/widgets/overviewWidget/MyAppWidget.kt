package com.zavedahmad.yaHabit.widgets.overviewWidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.zavedahmad.yaHabit.database.entities.HabitEntity
import com.zavedahmad.yaHabit.database.entities.state
import com.zavedahmad.yaHabit.database.repositories.HabitRepository
import com.zavedahmad.yaHabit.widgets.R
import com.zavedahmad.yahabit.common.WidgetUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.time.LocalDate

// todo filter out archived items
// todo open app when clicked on icon or on nothing page
// todo add partials in widget also
class HabitWidgetRepository(private val context: Context, val widgetUpdater: WidgetUpdater) {
    suspend fun update() {
        widgetUpdater.updateWidgets()
    }

}

class MyAppWidget : GlanceAppWidget(), KoinComponent {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Scaffold() {
                    val habitRepository: HabitRepository = get()
                    val habits = habitRepository.getHabitsFlowSortedByIndex()
                        .collectAsState(initial = emptyList())
                    Column(verticalAlignment = Alignment.CenterVertically) {
                        TitleBarWidget("Habits")
                        if (!habits.value.isEmpty()) {
                            HabitItemsList(habits.value, habitRepository)
                        }
                        else{
                            Box (GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                Text("Nothing Here", style = TextStyle(fontStyle = FontStyle.Italic , color = GlanceTheme.colors.onSurface))
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
private fun HabitItemsList(habits: List<HabitEntity>, habitRepository: HabitRepository) {
    LazyColumn {

        items(items = habits) { habit ->
            val habitCompletionEntity =
                habitRepository.getEntryOfCertainHabitIdAndDateFlow(
                    habit.id, LocalDate.now()
                ).collectAsState(initial = null).value
            var buttonAction: () -> Unit = {}
            var iconComposable: (@Composable () -> Unit) = {}
            val coroutineScope = rememberCoroutineScope()
            val date = LocalDate.now()
            var bgColor = GlanceTheme.colors.error
            var textColor = GlanceTheme.colors.onPrimary
            when (habitCompletionEntity?.state()) {

                "absolute" -> {
                    bgColor = GlanceTheme.colors.primary
                    buttonAction = {
                        coroutineScope.launch(Dispatchers.IO) {
                            habitRepository.setSkip(
                                date = date,
                                habitId = habit.id,
                                skipValue = true
                            )
                        }
                    }

                    iconComposable = {
                        Image(
                            provider = ImageProvider(R.drawable.outline_check_small_24),
                            contentDescription = "Done"
                        )
                    }
                }

                "skip" -> {
                    bgColor = GlanceTheme.colors.tertiary
                    buttonAction = {
                        coroutineScope.launch(
                            Dispatchers.IO
                        ) {
                            habitRepository.applyRepetitionForADate(
                                date = date,
                                habitId = habit.id,
                                newRepetitionValue = 0.0
                            )

                            habitRepository.setSkip(
                                date = date,
                                habitId = habit.id,
                                skipValue = false
                            )

                        }
                    }
                    iconComposable = {
                        Image(
                            provider = ImageProvider(R.drawable.outline_keyboard_double_arrow_right_24),
                            contentDescription = "Skipped"
                        )
                    }
                }

                "empty" -> {
                    textColor = GlanceTheme.colors.onSurface
                    bgColor = GlanceTheme.colors.surfaceVariant
                    buttonAction = {
                        coroutineScope.launch {
                            habitRepository.applyRepetitionForADate(
                                date = date,
                                habitId = habit.id,
                                newRepetitionValue = habit.repetitionPerDay
                            )
                        }
                    }
                    iconComposable = {
                        Image(
                            provider = ImageProvider(R.drawable.outline_close_small_24),
                            contentDescription = "Failed",
                            colorFilter = ColorFilter.tint(textColor)
                        )
                    }
                }

                else -> { // Includes null or any other state
                    textColor = GlanceTheme.colors.onSurface
                    bgColor = GlanceTheme.colors.surfaceVariant
                    buttonAction = {
                        coroutineScope.launch {
                            habitRepository.applyRepetitionForADate(
                                date = date,
                                habitId = habit.id,
                                newRepetitionValue = habit.repetitionPerDay
                            )
                        }
                    }
                    iconComposable = {
                        Image(
                            provider = ImageProvider(R.drawable.outline_close_small_24),
                            contentDescription = "Pending",
                            colorFilter = ColorFilter.tint(textColor)
                        )
                    }
                }
            }

            Column {
                Row(
                    GlanceModifier.background(bgColor)
                        .cornerRadius(10.dp)
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .fillMaxWidth().clickable { buttonAction() },

                    ) {
                    iconComposable()

                    Spacer(GlanceModifier.width(10.dp))
                    Text(text = habit.name, maxLines = 1, style = TextStyle(color = textColor))

                }
                Spacer(GlanceModifier.height(10.dp))

            }
        }

    }
}

@Composable
internal fun TitleBarWidget(title: String) {
    Row(GlanceModifier.padding(vertical = 10.dp, horizontal = 10.dp)) {

        Image(
            modifier = GlanceModifier.size(24.dp), provider =
                ImageProvider(R.drawable.yahabiticonnobg),
            contentDescription = "app Logo"
        )
        Spacer(GlanceModifier.width(20.dp))
        Text(
            title, maxLines = 1, style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
