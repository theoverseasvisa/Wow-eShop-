package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueDeep,
    onPrimaryContainer = SmoothWhite,
    secondary = HighContrastSilver,
    onSecondary = DeepBlackBg,
    secondaryContainer = PremiumSilverBorder,
    onSecondaryContainer = SmoothWhite,
    tertiary = MetallicGold,
    onTertiary = DeepBlackBg,
    background = DeepBlackBg,
    onBackground = SmoothWhite,
    surface = LuxuryCharcoal,
    onSurface = SmoothWhite,
    surfaceVariant = LuxuryGlassOverlay,
    onSurfaceVariant = HighContrastSilver,
    outline = PremiumSilverBorder,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalBlueGlow,
    onPrimaryContainer = DeepBlackBg,
    secondary = RoyalBlueDeep,
    onSecondary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = DeepBlackBg,
    surface = Color.White,
    onSurface = DeepBlackBg,
    outline = HighContrastSilver
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark luxury theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
