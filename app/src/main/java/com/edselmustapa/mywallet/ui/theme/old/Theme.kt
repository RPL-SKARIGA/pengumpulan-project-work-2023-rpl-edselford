package com.edselmustapa.mywallet.ui.theme.old

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColors = lightColorScheme(
    primary = oldColor.md_theme_light_primary,
    onPrimary = oldColor.md_theme_light_onPrimary,
    primaryContainer = oldColor.md_theme_light_primaryContainer,
    onPrimaryContainer = oldColor.md_theme_light_onPrimaryContainer,
    secondary = oldColor.md_theme_light_secondary,
    onSecondary = oldColor.md_theme_light_onSecondary,
    secondaryContainer = oldColor.md_theme_light_secondaryContainer,
    onSecondaryContainer = oldColor.md_theme_light_onSecondaryContainer,
    tertiary = oldColor.md_theme_light_tertiary,
    onTertiary = oldColor.md_theme_light_onTertiary,
    tertiaryContainer = oldColor.md_theme_light_tertiaryContainer,
    onTertiaryContainer = oldColor.md_theme_light_onTertiaryContainer,
    error = oldColor.md_theme_light_error,
    errorContainer = oldColor.md_theme_light_errorContainer,
    onError = oldColor.md_theme_light_onError,
    onErrorContainer = oldColor.md_theme_light_onErrorContainer,
    background = oldColor.md_theme_light_background,
    onBackground = oldColor.md_theme_light_onBackground,
    surface = oldColor.md_theme_light_surface,
    onSurface = oldColor.md_theme_light_onSurface,
    surfaceVariant = oldColor.md_theme_light_surfaceVariant,
    onSurfaceVariant = oldColor.md_theme_light_onSurfaceVariant,
    outline = oldColor.md_theme_light_outline,
    inverseOnSurface = oldColor.md_theme_light_inverseOnSurface,
    inverseSurface = oldColor.md_theme_light_inverseSurface,
    inversePrimary = oldColor.md_theme_light_inversePrimary,
    surfaceTint = oldColor.md_theme_light_surfaceTint,
    outlineVariant = oldColor.md_theme_light_outlineVariant,
    scrim = oldColor.md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = oldColor.md_theme_dark_primary,
    onPrimary = oldColor.md_theme_dark_onPrimary,
    primaryContainer = oldColor.md_theme_dark_primaryContainer,
    onPrimaryContainer = oldColor.md_theme_dark_onPrimaryContainer,
    secondary = oldColor.md_theme_dark_secondary,
    onSecondary = oldColor.md_theme_dark_onSecondary,
    secondaryContainer = oldColor.md_theme_dark_secondaryContainer,
    onSecondaryContainer = oldColor.md_theme_dark_onSecondaryContainer,
    tertiary = oldColor.md_theme_dark_tertiary,
    onTertiary = oldColor.md_theme_dark_onTertiary,
    tertiaryContainer = oldColor.md_theme_dark_tertiaryContainer,
    onTertiaryContainer = oldColor.md_theme_dark_onTertiaryContainer,
    error = oldColor.md_theme_dark_error,
    errorContainer = oldColor.md_theme_dark_errorContainer,
    onError = oldColor.md_theme_dark_onError,
    onErrorContainer = oldColor.md_theme_dark_onErrorContainer,
    background = oldColor.md_theme_dark_background,
    onBackground = oldColor.md_theme_dark_onBackground,
    surface = oldColor.md_theme_dark_surface,
    onSurface = oldColor.md_theme_dark_onSurface,
    surfaceVariant = oldColor.md_theme_dark_surfaceVariant,
    onSurfaceVariant = oldColor.md_theme_dark_onSurfaceVariant,
    outline = oldColor.md_theme_dark_outline,
    inverseOnSurface = oldColor.md_theme_dark_inverseOnSurface,
    inverseSurface = oldColor.md_theme_dark_inverseSurface,
    inversePrimary = oldColor.md_theme_dark_inversePrimary,
    surfaceTint = oldColor.md_theme_dark_surfaceTint,
    outlineVariant = oldColor.md_theme_dark_outlineVariant,
    scrim = oldColor.md_theme_dark_scrim,
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}