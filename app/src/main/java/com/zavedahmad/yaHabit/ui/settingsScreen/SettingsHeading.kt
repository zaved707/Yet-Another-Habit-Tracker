package com.zavedahmad.yaHabit.ui.settingsScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsHeading(text: String, topPadding: Boolean = true) {
    if (topPadding) {
        Spacer(Modifier.Companion.height(30.dp))
    }
    Row {
        Spacer(Modifier.Companion.width(30.dp))
        Spacer(Modifier.Companion.width(54.dp))
        Text(
            text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Companion.Medium,
            letterSpacing = 3.sp
        )
    }

}