package com.zavedahmad.yaHabit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

private val DarkColorScheme = darkColorScheme(

)

data class OutlineSizes(
    val small: Dp = 0.5.dp,
    val medium: Dp = 1.dp,
    val large: Dp = 2.dp
)

// Define CompositionLocal for outline sizes
val LocalOutlineSizes = staticCompositionLocalOf { OutlineSizes() }

@Composable
fun CustomTheme(
    primaryColor: Color? = null,
    secondaryColor: Color? = null,
    tertiaryColor: Color? = null,
    isAmoled: Boolean = true,
    theme: String,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    useExistingTheme : Boolean = false,
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

    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    } else {
        val realPrimaryColor = primaryColor ?: MaterialTheme.colorScheme.primary


            rememberDynamicColorScheme(
                primary = realPrimaryColor,

//                secondary = secondaryColor ?: MaterialTheme.colorScheme.secondary,
//                tertiary = tertiaryColor ?: MaterialTheme.colorScheme.tertiary,
//                neutral = MaterialTheme.colorScheme.surface,
                isDark = darkTheme,
                isAmoled = isAmoled,
                specVersion = ColorSpec.SpecVersion.SPEC_2025,

              contrastLevel = Contrast.Medium.value// 0.0 for normal contrast, 0.5 for medium, 1.0 for high
            )


    }

    CompositionLocalProvider(LocalOutlineSizes provides OutlineSizes() ) {

    MaterialTheme(
        colorScheme = colorScheme,


        content = content
    )
}}

