package com.zavedahmad.yaHabit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.rememberDynamicColorScheme

private val DarkColorScheme = darkColorScheme(

)


@Composable
fun CustomTheme(
    primaryColor: Color? = null,
    secondaryColor: Color? = null,
    tertiaryColor: Color? = null,
    theme: String,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = if (theme == "light") {
        false
    } else if (theme == "dark") {
        true
    } else if (theme == "system") {
        isSystemInDarkTheme()
    } else {
        isSystemInDarkTheme() // Optional: default case for unexpected theme values
    }




    MaterialTheme(
        colorScheme = rememberDynamicColorScheme(
            primary = primaryColor ?: MaterialTheme.colorScheme.primary,
            secondary = secondaryColor ?: MaterialTheme.colorScheme.secondary,
            tertiary = tertiaryColor ?: MaterialTheme.colorScheme.tertiary,
            isDark = darkTheme
        ),
        content = content
    )
}