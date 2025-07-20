package com.zavedahmad.yaHabit.ui.mainPage


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayItem3(
    date: LocalDate,
    state: String,
    repetitionsOnThisDay: Double ,
    addHabit: () -> Unit = {},
    deleteHabit: () -> Unit = {},
    dialogueComposable : @Composable (Boolean, ()-> Unit)-> Unit
) {
    val isDialogVisible = remember { mutableStateOf(false) }
    var bgColor = MaterialTheme.colorScheme.error
    var textColor = MaterialTheme.colorScheme.onError
    var borderColor = MaterialTheme.colorScheme.primary
    var buttonAction = {}
    var icon: ImageVector? = null
    var iconComposable: (@Composable () -> Unit) = { }
    dialogueComposable(isDialogVisible.value, {isDialogVisible.value = false})
    when (state) {
        "absolute" -> {
            buttonAction = deleteHabit
            bgColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.onPrimary
            borderColor = MaterialTheme.colorScheme.primary
            icon = Icons.Default.Check
//            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }
        iconComposable = {Text(repetitionsOnThisDay.toString(), color = textColor)}
        }


        "partial" -> {
            buttonAction = addHabit
            bgColor = MaterialTheme.colorScheme.primary.copy(0.5f)
            textColor = MaterialTheme.colorScheme.onBackground.copy(0.6f)
            borderColor = MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }

        }

        "absoluteDisabled" -> {

            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
            textColor = MaterialTheme.colorScheme.inverseSurface.copy(0.8f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.1f)
            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }


        }

        "partialDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)
            textColor = MaterialTheme.colorScheme.inverseSurface.copy(0.4f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)
            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }


        }

        "incompleteDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }


        }

        "incomplete" -> {
            borderColor = MaterialTheme.colorScheme.primary
            buttonAction = addHabit
            bgColor = MaterialTheme.colorScheme.surfaceVariant
            textColor = MaterialTheme.colorScheme.onSurface
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }

        }

    }
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()

                .border(width = 0.1.dp, color = borderColor),


        ) {
        Box(
            Modifier.combinedClickable(
                onClick = {isDialogVisible.value =true},
                onDoubleClick = {},
                onLongClick = { buttonAction() }).background(bgColor)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    /* Text(
                         date.dayOfWeek.name.slice(0..2),
                         style = MaterialTheme.typography.labelSmall
                     )*/
                    Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.labelLarge, color = textColor)
                }
                HorizontalDivider()
                Column(
                    Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    iconComposable()
                }
            }
        }
    }
}