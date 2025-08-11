package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zavedahmad.yaHabit.ui.components.CardMyStyle
import java.time.DayOfWeek

@Composable
fun ModalForWeekSelection(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onDaySelect: (DayOfWeek) -> Unit,
    currentlySelectedDay: DayOfWeek = DayOfWeek.SUNDAY
) {

    if (isVisible) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            CardMyStyle(modifier = Modifier.Companion.verticalScroll(rememberScrollState())) {
                Column(Modifier.Companion.fillMaxWidth(0.7f)) {
                    for (i in 1..7) {
                        val currentDay = DayOfWeek.of(i)
                        Box(Modifier.Companion.combinedClickable(onClick = { onDaySelect(currentDay) })) {
                            Row(
                                Modifier.Companion
                                    .padding(20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    currentDay.toString()
                                )
                                if (currentDay == currentlySelectedDay) {
                                    Icon(Icons.Default.Check, contentDescription = "f")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}