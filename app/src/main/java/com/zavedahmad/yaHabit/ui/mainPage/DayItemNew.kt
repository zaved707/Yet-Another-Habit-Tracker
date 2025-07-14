package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayItemNew(
    date: LocalDate,
    state: String,
    addHabit: () -> Unit = {},
    deleteHabit: () -> Unit = {}
) {
    var bgColor = MaterialTheme.colorScheme.error
    var textColor = MaterialTheme.colorScheme.onError
    var borderColor = MaterialTheme.colorScheme.primary
    var buttonAction = {}
    when (state) {
        "absolute" -> {
            buttonAction = deleteHabit
            bgColor = MaterialTheme.colorScheme.primary
            textColor = MaterialTheme.colorScheme.onPrimary
            borderColor = MaterialTheme.colorScheme.primary
        }


        "partial" -> {
            buttonAction = addHabit
            bgColor = MaterialTheme.colorScheme.primary.copy(0.5f)
            textColor = MaterialTheme.colorScheme.onBackground.copy(0.6f)
            borderColor = MaterialTheme.colorScheme.primaryContainer.copy(0.5f)

        }

        "absoluteDisabled" -> {

            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.5f)
            textColor = MaterialTheme.colorScheme.inverseSurface.copy(0.8f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.1f)

        }
        "partialDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)
            textColor = MaterialTheme.colorScheme.inverseSurface.copy(0.4f)
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.3f)


        }

        "incompleteDisabled" -> {
            bgColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)
            textColor = MaterialTheme.colorScheme.onSurfaceVariant
            borderColor = MaterialTheme.colorScheme.inverseSurface.copy(0.05f)


        }

        "incomplete" -> {
            borderColor = MaterialTheme.colorScheme.primary
            buttonAction = addHabit
            bgColor = MaterialTheme.colorScheme.surfaceVariant
            textColor = MaterialTheme.colorScheme.onSurface

        }

    }
    Box(
        Modifier
            .fillMaxWidth(),

        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = bgColor
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        buttonAction()
                    },
                    hapticFeedbackEnabled = true
                )
                .size(35.dp),
            border = BorderStroke(
                width = 3.dp,
                color = borderColor
            )
        ) {
            Column(
                Modifier
                    .fillMaxSize(),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    date.dayOfMonth.toString(),
                    color = textColor
                )
            }
        }
    }

}