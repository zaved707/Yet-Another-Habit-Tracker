package com.zavedahmad.yaHabit.ui.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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

            Icon(
                Icons.AutoMirrored.Filled.FormatListBulleted,
                modifier = Modifier.size(200.dp),
                contentDescription = "placeholderr",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "No Habits In this List",
                textAlign = TextAlign.Center
            )


        }
    }

}