package com.zavedahmad.yaHabit.ui.errorPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zavedahmad.yaHabit.R


@Composable
fun SplashScreen() {
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center){
        Image(

            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "splash",
            modifier = Modifier.size(300.dp)
        )
    }

}