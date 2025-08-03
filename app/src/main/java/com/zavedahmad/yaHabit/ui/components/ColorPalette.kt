package com.zavedahmad.yaHabit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColorPalette() {
    Row {
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        Box(
            Modifier
                .weight(1f)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        )
        //                    Box(Modifier.weight(1f).height(30.dp).background(MaterialTheme.colorScheme.s))
    }

}