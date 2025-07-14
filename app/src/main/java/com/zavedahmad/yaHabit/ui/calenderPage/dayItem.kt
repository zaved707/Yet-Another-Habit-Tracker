package com.zavedahmad.yaHabit.ui.calenderPage

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
import androidx.compose.ui.unit.dp
import com.materialkolor.ktx.darken
import com.materialkolor.ktx.lighten
import java.time.LocalDate

@Composable
fun DayItem(
    date: LocalDate,
    state: String,
    addHabit: () -> Unit = {},
    deleteHabit: () -> Unit= {}
) {



    var bgColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        Modifier
            .fillMaxWidth()
          ,

        contentAlignment = Alignment.Center
    ) {
        if (state == "partial") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            addHabit()
                        },
                        hapticFeedbackEnabled = true
                    )
                    .size(35.dp),
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }


        } else if (state == "error") {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)

                    .size(35.dp)

            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else if (state == "absolute") {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            deleteHabit()
                        }, hapticFeedbackEnabled = true
                    )
                    .size(35.dp)

            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else if (state == "disabled") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.darken(
                        2f
                    )
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)

                    .size(35.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceBright)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onSurface.darken(4f)
                    )
                }
            }

        } else if (state == "absoluteDisabled") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright.lighten(1.5f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)

                    .size(35.dp)

            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onSurface.darken(2f)
                    )
                }
            }

        } else if (state == "partialDisabled") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)

                    .size(35.dp)

            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.darken(2f)
                    )
                }
            }
        } else if (state == "incompleteDisabled") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.darken(
                        2f
                    )
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)

                    .size(35.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceBright)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        color = MaterialTheme.colorScheme.onSurface.darken(4f)
                    )
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            addHabit()
                        },
                        hapticFeedbackEnabled = true
                    )
                    .size(35.dp),
                border = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(date.dayOfMonth.toString())
                }
            }
        }

    }

}

