package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import com.zavedahmad.yaHabit.roomDatabase.HabitEntity
import com.zavedahmad.yaHabit.roomDatabase.isSkip
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun DialogueForHabit(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    habitCompletionEntity: HabitCompletionEntity?,
    updateHabitCompletionEntity: (HabitCompletionEntity) -> Unit,
    habitEntity: HabitEntity,
    onFinalised: (isRepetitionsChanged: Boolean, isNotesChanged: Boolean, userTypedRepetition: String, userTypedNote: String?) -> Unit
) {
    if (isVisible) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            val entityAlreadyExists = habitCompletionEntity != null
            val repetitions = remember {
                mutableStateOf(
                    if (entityAlreadyExists) {
                        if (habitCompletionEntity.repetitionsOnThisDay % 1.0 == 0.0) {
                            habitCompletionEntity.repetitionsOnThisDay.toLong().toString()
                        } else {
                            habitCompletionEntity.repetitionsOnThisDay.toString()
                        }
                    } else {
                        ""
                    }
                )
            }
            val isSkip = if (entityAlreadyExists) {
                habitCompletionEntity.isSkip()
            } else {
                false
            }
            val isRepetitionsValueValid =
                remember { derivedStateOf { repetitions.value.toDoubleOrNull() != null } }
            val isRepetitionsValueChanged = remember {
                derivedStateOf {
                    if (isRepetitionsValueValid.value) {
                        if (entityAlreadyExists) {
                            repetitions.value.toDouble() != habitCompletionEntity.repetitionsOnThisDay

                        } else {
                            repetitions.value.toDouble() > 0.0
                        }
                    } else {
                        false
                    }
                }
            }
            val note = remember {
                mutableStateOf(
                    if (entityAlreadyExists) {
                        habitCompletionEntity.note
                    } else {
                        null
                    }
                )
            }
            val isNoteValueChanged = remember {
                derivedStateOf {
                    if (entityAlreadyExists) {
                        note.value != habitCompletionEntity.note
                    } else {
                        note.value != ""
                    }
                }
            }
            val isAnyValueChanged =
                remember {
                    derivedStateOf {

                        isNoteValueChanged.value || isRepetitionsValueChanged.value

                    }
                }

            Card {
                Column(
                    modifier = Modifier.Companion.padding(20.dp)


                ) {
                    TextField(
                        value = note.value ?: "",
                        onValueChange = {
                            note.value = if (it == "") {
                                null
                            } else {
                                it
                            }
                        },
                        placeholder = {
                            Text(
                                "Note",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        })
                    if (!isSkip) {
                        TextField(
                            value = repetitions.value,
                            onValueChange = {
                                if (it != "") {
                                    it.toDoubleOrNull()?.let { it1 ->
                                        if (it1 <= 1000000.0) {
                                            repetitions.value =
                                                it
                                        }
                                    }
                                } else {
                                    repetitions.value = ""
                                }

                            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), maxLines = 1,
                            placeholder = {Text("Repetitions")}
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                Row(
                    Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(

                        onClick = {
                            onFinalised(
                                isRepetitionsValueChanged.value,
                                isNoteValueChanged.value,
                                repetitions.value,
                                note.value
                            )

                            onDismissRequest()


                        }) { Text("Apply") }
                }
            }
        }}
    }
}
