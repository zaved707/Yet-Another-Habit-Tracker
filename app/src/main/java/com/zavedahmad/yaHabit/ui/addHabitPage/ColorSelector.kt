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
    val colors = listOf<Color>(
        Color(0xFFFF69B4), // Vibrant Pink (HotPink)
        Color(0xFFFF7F50), // Vibrant Coral (Coral)
        Color(0xFFBA55D3), // Vibrant Orchid (MediumOrchid)
        Color(0xFF9370DB), // Vibrant Medium Purple (MediumPurple)
        Color(0xFF4682B4), // Vibrant Steel Blue (SteelBlue)
        Color(0xFF00BFFF), // Vibrant Deep Sky Blue (DeepSkyBlue)
        Color(0xFF40E0D0), // Vibrant Turquoise (Turquoise)
        Color(0xFF00FA9A), // Vibrant Medium Spring Green (MediumSpringGreen)
        Color(0xFF32CD32), // Vibrant Lime Green (LimeGreen)
        Color(0xFF7FFF00), // Vibrant Chartreuse (Chartreuse)
        Color(0xFFADFF2F), // Vibrant Green Yellow (GreenYellow)
        Color(0xFFFFFF00), // Vibrant Yellow (Yellow)
        Color(0xFFFFD700), // Vibrant Gold (Gold)
        Color(0xFFFFA500), // Vibrant Orange (Orange)
        Color(0xFFFF8C00), // Vibrant Dark Orange (DarkOrange)
        Color(0xFFFF4500), // Vibrant Orange Red (OrangeRed)
        Color(0xFFA0522D), // Vibrant Sienna (Sienna)
        Color(0xFF778899), // Vibrant Light Slate Gray (LightSlateGray)
        Color(0xFF708090)  // Vibrant Slate Gray (SlateGray)
    )
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