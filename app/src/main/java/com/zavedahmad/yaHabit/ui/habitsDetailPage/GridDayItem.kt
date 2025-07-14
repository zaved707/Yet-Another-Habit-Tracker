package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun GridDayItem(
    state: String = "error",
    addHabit: () -> Unit = {},
    date: LocalDate,
    deleteHabit: () -> Unit = {},
    showDate: Boolean = false,
    interactive: Boolean = false
) {
    var buttonAction = {}
    val (bgColor, textColor) = when (state) {
        "absolute" -> {
            buttonAction = deleteHabit
            Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
        }

        "absoluteDisabled" -> {
            buttonAction = addHabit
            Pair(
                MaterialTheme.colorScheme.inverseSurface.copy(0.8f),
                MaterialTheme.colorScheme.inverseOnSurface
            )
        }


        "partial" -> {
            buttonAction = addHabit
            Pair(
                MaterialTheme.colorScheme.primary.copy(0.5f),
                MaterialTheme.colorScheme.onBackground.copy(0.6f)
            )
        }

        "partialDisabled" -> {

            Pair(
                MaterialTheme.colorScheme.inverseSurface.copy(0.3f),
                MaterialTheme.colorScheme.inverseOnSurface
            )
        }


        "incompleteDisabled" -> {

            Pair(
                MaterialTheme.colorScheme.inverseSurface.copy(0.05f),
                MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        "incomplete" -> {
            buttonAction = addHabit
            Pair(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        else -> {
            buttonAction = addHabit
            Pair(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
        }
    }

    val modifier = if (interactive) {
        Modifier.combinedClickable(onClick = {}, onLongClick = { buttonAction() })
    } else Modifier

    Box(
        modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (showDate) {
            Text(date.dayOfMonth.toString(), color = textColor)
        }

    }
}