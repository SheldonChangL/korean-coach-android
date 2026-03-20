package com.koreancoach.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = KoreanRed,
    onPrimary = OnKoreanRed,
    primaryContainer = KoreanRedContainer,
    onPrimaryContainer = OnKoreanRedContainer,
    secondary = GoldAccent,
    onSecondary = OnGoldAccent,
    secondaryContainer = GoldAccentContainer,
    onSecondaryContainer = OnGoldAccentContainer,
    tertiary = KoreanBlue,
    onTertiary = OnKoreanBlue,
    tertiaryContainer = KoreanBlueContainer,
    onTertiaryContainer = OnKoreanBlueContainer,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    background = Background,
    onBackground = OnSurface,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = ErrorRed,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = KoreanRedDark,
    onPrimary = OnKoreanRedContainer,
    primaryContainer = KoreanRedContainerDark,
    onPrimaryContainer = KoreanRedDark,
    secondary = GoldAccent,
    onSecondary = OnGoldAccent,
    tertiary = KoreanBlueDark,
    onTertiary = OnKoreanBlueContainer,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = OnSurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark
)

@Composable
fun KoreanCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
