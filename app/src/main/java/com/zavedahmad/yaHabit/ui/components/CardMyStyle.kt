package com.zavedahmad.yaHabit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardMyStyle(modifier : Modifier = Modifier, content :  @Composable () -> Unit) {
    OutlinedCard(
        modifier= modifier

           ,

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
    ) {
        content()
    }
}