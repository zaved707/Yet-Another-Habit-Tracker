package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ColorSelector(viewModel: AddHabitPageViewModel){
    val setColor = viewModel.selectedColor.collectAsStateWithLifecycle()
    val colors = viewModel.colors
    Card(
        Modifier
            .height(100.dp)
            .fillMaxWidth()

    ) {
        LazyRow( verticalAlignment = Alignment.CenterVertically) {

            items(colors.size) {index ->
                val color = colors[index]
                if (index == 0){
                    Spacer(Modifier.width(10.dp))
                }
                Box(
                    Modifier
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxHeight()
                        .size(60.dp)
                ) {
                    if (setColor.value == color) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .clickable(onClick = {})
                        ) {
                            Box(
                                Modifier
                                    .padding(4.dp)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "selected Color"
                                )
                            }
                        }
                    } else {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(color)
                                .clickable(onClick = { viewModel.setColor(color) })
                        )
                    }
                }
                if (index == colors.size -1){
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
}