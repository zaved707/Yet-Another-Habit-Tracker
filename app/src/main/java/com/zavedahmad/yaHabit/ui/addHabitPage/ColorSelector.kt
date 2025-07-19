@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.zavedahmad.yaHabit.ui.addHabitPage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zavedahmad.yaHabit.ui.theme.CustomTheme

@Composable
fun ColorSelector(viewModel: AddHabitPageViewModel) {
    val setColor = viewModel.selectedColor.collectAsStateWithLifecycle()
    val colors = viewModel.colors
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    Card(
        Modifier
            .height(100.dp)
            .fillMaxWidth()

    ) {

        LazyRow(verticalAlignment = Alignment.CenterVertically) {

            items(colors.size) { index ->
                val color = colors[index]
                val isSelected = setColor.value == color
                if (index == 0) {
                    Spacer(Modifier.width(10.dp))
                }
                val modifierForClicking = if (!isSelected) {
                    Modifier.clickable(onClick = { viewModel.setColor(color) })
                } else {
                    Modifier
                }
                val progress by animateFloatAsState(
                    targetValue = if (isSelected) {
                        0.7f
                    } else {
                        0.5f
                    }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
//                        tween(durationMillis = 700)
                )

                Box(
                    Modifier
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                        .fillMaxHeight()
                        .size(60.dp), contentAlignment = Alignment.Center
                ) {

                    CustomTheme(primaryColor = color, theme = theme?.value ?: "system") {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(MaterialShapes.Cookie12Sided.toShape())
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialShapes.Cookie12Sided.toShape()
                                )
                                .background(MaterialTheme.colorScheme.surfaceDim),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = modifierForClicking.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxSize(progress)
                                        .clip(MaterialShapes.Cookie4Sided.toShape())
//                                        .border(
//                                            2.dp,
//                                            MaterialTheme.colorScheme.onPrimary,
//                                            MaterialShapes.Cookie4Sided.toShape()
//                                        )
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center

                                ) {
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            "",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }

                                }
                            }
                        }


                    }


//                            Box(
//                                Modifier
//                                    .fillMaxSize()
//                                    .clip(RoundedCornerShape(10.dp))
//                                    .background(MaterialTheme.colorScheme.primary)
//
//                            )


                }

            }
            item { Spacer(Modifier.width(10.dp)) }
        }
    }
}