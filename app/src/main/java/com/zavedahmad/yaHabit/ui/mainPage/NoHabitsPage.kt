package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3ExpressiveApi
@Composable
fun NoHabitsPage(modifier: Modifier) {
    val pagerState = rememberPagerState(pageCount = {
        1
    })
    VerticalPager(state = pagerState) {
        Column(
            modifier = modifier
                .fillMaxSize()
              ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .clip(MaterialShapes.Cookie12Sided.toShape())
                    .background(
                        MaterialTheme.colorScheme.primary.copy(0.2f)
                    ) .border(
                        border = BorderStroke(
                            width = 5.dp,
                            brush = SolidColor(
                                MaterialTheme.colorScheme.primary.copy(
                                    0.5f
                                )
                            )
                        ), shape = MaterialShapes.Cookie12Sided.toShape()
                    )
                    .padding(30.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.FormatListBulleted,
                    modifier = Modifier.size(200.dp),
                    contentDescription = "placeholderr",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "No Habits In this List",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )


        }
    }

}