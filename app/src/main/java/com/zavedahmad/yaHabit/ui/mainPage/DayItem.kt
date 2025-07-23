package com.zavedahmad.yaHabit.ui.mainPage


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.R
import java.time.LocalDate

// TODO make disabled buttons untouchable
// TODO Fix this please according to new way
@Composable
fun DayItem(
    date: LocalDate,
    state: String,
    repetitionsOnThisDay: Double,
    addHabit: () -> Unit = {},
    deleteHabit: () -> Unit = {},
    skipHabit: () -> Unit,
    unSkipHabit: () -> Unit,
    hasNote: Boolean = false,
    dialogueComposable: @Composable (Boolean, () -> Unit) -> Unit,
    interactive: Boolean = false
) {
    val isDialogVisible = remember { mutableStateOf(false) }
    var bgColor = MaterialTheme.colorScheme.error
    var textColor = MaterialTheme.colorScheme.onError
    var borderColor = MaterialTheme.colorScheme.primary
    var buttonAction: List<() -> Unit> = listOf({}, {})
    var icon: ImageVector? = null
    var iconComposable: (@Composable () -> Unit) = { }
    dialogueComposable(isDialogVisible.value, { isDialogVisible.value = false })
    when (state) {
        "absolute" -> {
            buttonAction = listOf(skipHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.primary
            borderColor = MaterialTheme.colorScheme.primary
            icon = Icons.Default.Check
//            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }
            iconComposable =
                { Text(repetitionsOnThisDay.toString(), color = textColor, maxLines = 1) }
        }


        "partial" -> {
            buttonAction = listOf(addHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.primary.copy(0.5f)
            textColor = MaterialTheme.colorScheme.primary
            borderColor = MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
            iconComposable = {
                Image(
                    painter = painterResource(R.drawable.hollowtick),
                    contentDescription = "hollow tick",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(0.8f))
                )
            }

        }

        "absoluteDisabled" -> {

            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
            textColor = MaterialTheme.colorScheme.onSurface.copy(0.5f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.1f)
            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }


        }

        "partialDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)
            textColor = MaterialTheme.colorScheme.inverseSurface.copy(0.4f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)
            iconComposable = { Icon(Icons.Default.Check, "", tint = textColor) }


        }

        "incompleteDisabled", "emptyDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            textColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }

        }

        "noteDisabled" -> {
            bgColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.3f)
            textColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.7f)
            borderColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.1f)
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }
        }

        "incomplete", "empty" -> {
            borderColor = MaterialTheme.colorScheme.primary
            buttonAction = listOf(addHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.onSurfaceVariant
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }

        }

        "note" -> {
            borderColor = MaterialTheme.colorScheme.primary
            buttonAction = listOf(addHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.secondaryContainer
            textColor = MaterialTheme.colorScheme.onTertiaryContainer
            iconComposable = { Icon(Icons.Default.Close, "", tint = textColor) }
        }

        "skip" -> {
            borderColor = MaterialTheme.colorScheme.primary
            buttonAction = listOf(unSkipHabit, { isDialogVisible.value = true })
            bgColor = MaterialTheme.colorScheme.secondaryContainer
            textColor = MaterialTheme.colorScheme.secondary.copy(0.9f)
            iconComposable = { Icon(Icons.Default.DoubleArrow, "", tint = textColor) }
        }

    }
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()


    ) {
        Surface(
            Modifier


                .padding(horizontal = 3.dp),
            tonalElevation = 1.dp,
            shape = RoundedCornerShape(10.dp),


            ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                if (hasNote){
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(5.dp),
                    tonalElevation = 5.dp,
                    shadowElevation = 100.dp,
                    color = MaterialTheme.colorScheme.tertiary
                ) {}}
                Column(
                    modifier =
                        if (interactive) {
                            Modifier.fillMaxSize().combinedClickable(
                                onLongClick = buttonAction[1],
                                onDoubleClick = { },
                                onClick = {
                                    buttonAction[0]()
                                    println("$state This is state")
                                }
                            )
                        } else Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        /* Text(
                             date.dayOfWeek.name.slice(0..2),
                             style = MaterialTheme.typography.labelSmall
                         )*/
                        Text(
                            date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = textColor
                        )
                    }

                    Column(
                        Modifier
                            .padding(5.dp)
                            .height(25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        iconComposable()
                    }
                }
            }
        }
    }
}