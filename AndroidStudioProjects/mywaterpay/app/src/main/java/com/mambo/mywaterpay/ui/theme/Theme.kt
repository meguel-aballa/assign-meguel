package com.mambo.mywaterpay.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = WaterPrimary,
    onPrimary = WaterOnPrimary,
    primaryContainer = WaterPrimaryContainer,
    onPrimaryContainer = WaterOnPrimaryContainer,
    secondary = WaterSecondary,
    onSecondary = WaterOnSecondary,
    secondaryContainer = WaterSecondaryContainer,
    onSecondaryContainer = WaterOnSecondaryContainer,
    tertiary = WaterTertiary,
    onTertiary = WaterOnTertiary,
    tertiaryContainer = WaterTertiaryContainer,
    onTertiaryContainer = WaterOnTertiaryContainer,
    error = WaterError,
    onError = WaterOnError,
    errorContainer = WaterErrorContainer,
    background = WaterBackground,
    onBackground = WaterOnBackground,
    surface = WaterSurface,
    onSurface = WaterOnSurface,
    surfaceVariant = WaterSurfaceVariant,
    onSurfaceVariant = WaterOnSurfaceVariant,
    outline = WaterOutline
)

private val LightColorScheme = lightColorScheme(
    primary = WaterPrimary,
    onPrimary = WaterOnPrimary,
    primaryContainer = WaterPrimaryContainer,
    onPrimaryContainer = WaterOnPrimaryContainer,
    secondary = WaterSecondary,
    onSecondary = WaterOnSecondary,
    secondaryContainer = WaterSecondaryContainer,
    onSecondaryContainer = WaterOnSecondaryContainer,
    tertiary = WaterTertiary,
    onTertiary = WaterOnTertiary,
    tertiaryContainer = WaterTertiaryContainer,
    onTertiaryContainer = WaterOnTertiaryContainer,
    error = WaterError,
    onError = WaterOnError,
    errorContainer = WaterErrorContainer,
    background = WaterBackground,
    onBackground = WaterOnBackground,
    surface = WaterSurface,
    onSurface = WaterOnSurface,
    surfaceVariant = WaterSurfaceVariant,
    onSurfaceVariant = WaterOnSurfaceVariant,
    outline = WaterOutline
)

@Composable
fun MyWaterPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme // Using DarkColorScheme even in light mode for the "Black" look
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
