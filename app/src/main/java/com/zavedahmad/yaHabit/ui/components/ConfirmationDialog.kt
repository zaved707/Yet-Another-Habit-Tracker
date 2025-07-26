package com.zavedahmad.yaHabit.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp


@Composable
fun ConfirmationDialog(
    visible: Boolean,
    text: String = "do you want to confirm this?",
    confirmText: String = "Confirm!",
    cancelText: String = "Cancel?",
    confirmAction: () -> Unit,
    onDismiss: () -> Unit,
    confirmationColor: ButtonColors = ButtonDefaults.buttonColors()
) {
    if (visible) {
        Dialog(onDismissRequest = { onDismiss() }) {
            OutlinedCard(
                modifier = Modifier

                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(30.dp)) {
                    OutlinedCard (colors = CardDefaults.outlinedCardColors()){
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text)
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        ) { Text(cancelText, color = MaterialTheme.colorScheme.onSurface) }
                        Spacer(Modifier.width(20.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = confirmationColor,
                            onClick = { confirmAction() }) { Text(confirmText) }
                    }
                }
            }
        }
    }
}