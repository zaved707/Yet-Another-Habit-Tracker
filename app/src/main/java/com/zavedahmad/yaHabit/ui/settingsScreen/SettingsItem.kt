package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsItem(
    icon: @Composable (() -> Unit) = { Column { Spacer(Modifier.Companion.size(24.dp)) } },
    title: String,
    description: String? = null,
    task: () -> Unit,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.Companion
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = { task() })
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ), // Use IntrinsicSize.Min to allow flexible height
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically // Center items vertically

    ) {
        Row(
            modifier = if (actions != null) {
                Modifier.Companion

                    .fillMaxWidth(0.7f)
            } else {
                Modifier.Companion.fillMaxWidth()
            },
            verticalAlignment = Alignment.Companion.CenterVertically
        ) { // Added weight to allow this Row to take available space
            Column(Modifier.Companion.padding(10.dp)) {
                icon()
            }
            Spacer(Modifier.Companion.width(20.dp))
            Column(Modifier.Companion.fillMaxWidth()) {
                Text(
                    title,
                    style = TextStyle(fontWeight = FontWeight.Companion.Bold, fontSize = 20.sp)
                )
                description?.let {
                    Text(
                        description,
                        style = TextStyle(
                            fontWeight = FontWeight.Companion.Normal,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        if (actions != null) {
            Row(

                verticalAlignment = Alignment.Companion.CenterVertically
            ) { // Added fillMaxHeight to the actions Row


                actions()
            }
        }
    }
}