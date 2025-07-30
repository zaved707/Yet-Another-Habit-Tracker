package com.zavedahmad.yaHabit.ui.components

import android.app.Dialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.ui.theme.LocalOutlineSizes

@Composable
fun DialogItem(
    modifier: Modifier = Modifier,
    state: Boolean,
    onValueChange: () -> Unit,
    content: @Composable () -> Unit
) {

    Box(
        modifier
            .clip(RoundedCornerShape(10.dp)).background(    MaterialTheme.colorScheme.surfaceDim)
            .border(shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    width = LocalOutlineSizes.current.small,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f)
                )
            )

            .combinedClickable(onClick = { onValueChange() })

    ) {

        content()

    }
}