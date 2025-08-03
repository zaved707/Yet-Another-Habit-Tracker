package com.zavedahmad.yaHabit.ui.habitsDetailPage

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import kotlin.collections.List

// Done implement note also
@Composable
fun GridDayItem(
    state: String = "error",
    addHabit: () -> Unit = {},
    date: LocalDate,
    deleteHabit: () -> Unit = {},
    showDate: Boolean = false,
    interactive: Boolean = false,
    skipHabit: () -> Unit,
    hasNote: Boolean = false,
    unSkipHabit: () -> Unit,
    dialogueComposable: @Composable (Boolean, () -> Unit) -> Unit
) {
    val isDialogVisible = remember { mutableStateOf(false) }
    var buttonAction: List<() -> Unit> = listOf({}, {})
    var textColor = MaterialTheme.colorScheme.onError
    dialogueComposable(isDialogVisible.value, { isDialogVisible.value = false })
    var bgColor: Color
    var noteIndicatorColor: Color
    when (state) {
        "absolute" -> {
            bgColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.onPrimary
            buttonAction = listOf(skipHabit, { isDialogVisible.value = true })
            noteIndicatorColor = MaterialTheme.colorScheme.onPrimary

        }

        "absoluteDisabled" -> {


            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.8f)
            textColor = MaterialTheme.colorScheme.onSurface
            noteIndicatorColor = MaterialTheme.colorScheme.surfaceVariant

        }


        "partial" -> {
            buttonAction = listOf(addHabit, { isDialogVisible.value = true })

            bgColor = MaterialTheme.colorScheme.primary.copy(0.5f)
            textColor = if (bgColor.luminance() > 0.5f) Color.Black else Color.White
            noteIndicatorColor = if (bgColor.luminance() > 0.5f) Color.Black else Color.White


        }

        "partialDisabled" -> {


            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.1f)
            textColor = MaterialTheme.colorScheme.inverseOnSurface
            noteIndicatorColor = MaterialTheme.colorScheme.surfaceVariant

        }


        "incompleteDisabled" -> {

            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            textColor = MaterialTheme.colorScheme.inverseOnSurface
            noteIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer

        }

        "incomplete", "empty" -> {
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
            buttonAction = listOf(addHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.surfaceVariant
            noteIndicatorColor = MaterialTheme.colorScheme.tertiary


        }

        "skip" -> {
            buttonAction = listOf(unSkipHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.tertiary
            textColor = MaterialTheme.colorScheme.onTertiary
            noteIndicatorColor = MaterialTheme.colorScheme.onTertiary

        }
        else -> {
            bgColor = MaterialTheme.colorScheme.error
            noteIndicatorColor = MaterialTheme.colorScheme.onError

        }


    }

    val modifier = if (interactive) {
        Modifier.combinedClickable(onClick = {
            buttonAction[0]()
            println("$state This is state")
        }, onLongClick = buttonAction[1])
    } else Modifier


    Box(
        modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = bgColor),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (hasNote && interactive) {
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .size(15.dp)
                    .padding(3.dp),

                shadowElevation = 100.dp,
                color = noteIndicatorColor
            ) {}

        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            if (showDate) {
                Text(date.dayOfMonth.toString(), color = textColor)
            }
        }
    }
}