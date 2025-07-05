package com.zavedahmad.yaHabit.ui.habitsDetailPage

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun GridDayItem(
    state: String = "error",
    addHabit : () -> Unit = {},
    date : LocalDate? = null,
    deleteHabit : () -> Unit = {},
) {
    var buttonAction = {}
    val bgColor = when (state) {
        "absolute" -> {
            buttonAction = deleteHabit
            MaterialTheme.colorScheme.primary
        } "absoluteDisabled" -> {
            buttonAction = addHabit
            MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
        }


        "partial" -> {
            buttonAction = addHabit
            MaterialTheme.colorScheme.primary.copy(0.5f)
        }
        "partialDisabled" -> {
            buttonAction = addHabit
            MaterialTheme.colorScheme.inverseSurface.copy(0.2f)
        }


        "incompleteDisabled" -> {buttonAction = addHabit
            MaterialTheme.colorScheme.surface.copy(0.5f)}
        "incomplete" -> {buttonAction = addHabit
            MaterialTheme.colorScheme.surface}
        else -> {
            buttonAction = addHabit
            MaterialTheme.colorScheme.error
        }
    }


    Box(
        Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(5.dp))

            .background(color = bgColor)
            .combinedClickable(onClick = {}, onLongClick = { buttonAction() }),

        contentAlignment = Alignment.Center
    ) {


    }
}