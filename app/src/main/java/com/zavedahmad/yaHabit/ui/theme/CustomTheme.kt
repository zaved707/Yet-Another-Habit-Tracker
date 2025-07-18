package com.zavedahmad.yaHabit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

private val DarkColorScheme = darkColorScheme(

)


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        if(useExistingTheme) {
            rememberDynamicColorScheme(
                primary = primaryColor ?: MaterialTheme.colorScheme.primary,
                secondary = secondaryColor ?: MaterialTheme.colorScheme.secondary,
                tertiary = tertiaryColor ?: MaterialTheme.colorScheme.tertiary,
                isDark = darkTheme,
                isAmoled = isAmoled,
                specVersion = ColorSpec.SpecVersion.SPEC_2025,
                style = PaletteStyle.Vibrant,
                contrastLevel = 0.5 // 0.0 for normal contrast, 0.5 for medium, 1.0 for high
            )
        }else{
            rememberDynamicColorScheme(
                primary = primaryColor ?: Purple80,
                secondary = secondaryColor ?: PurpleGrey80,
                tertiary = tertiaryColor ?: Pink80,
                isDark = darkTheme,
                isAmoled = isAmoled,
                specVersion = ColorSpec.SpecVersion.SPEC_2025,
                style = PaletteStyle.Vibrant,
                contrastLevel = 0.0 // 0.0 for normal contrast, 0.5 for medium, 1.0 for high
            )
        }

    }


    MaterialExpressiveTheme(
        colorScheme = colorScheme,


        content = content
    )
}