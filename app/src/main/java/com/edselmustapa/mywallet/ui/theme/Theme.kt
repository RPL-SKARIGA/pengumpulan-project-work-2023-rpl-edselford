
package com.edselmustapa.mywallet.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MyWalletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography.copy(
//            displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.TitilliumWeb),
//            displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.TitilliumWeb),
//            displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.TitilliumWeb),
//
//            headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.TitilliumWeb),
//            headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.TitilliumWeb),
//            headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.TitilliumWeb),
//
//            titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.TitilliumWeb),
//            titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.TitilliumWeb),
//            titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.TitilliumWeb),
//
//            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.TitilliumWeb),
//            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.TitilliumWeb),
//            bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.TitilliumWeb),
//
//            labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.TitilliumWeb),
//            labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.TitilliumWeb),
//            labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.TitilliumWeb)
        ),
        content = content
    )
}