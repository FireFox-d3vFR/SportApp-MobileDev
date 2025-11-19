package com.example.sportapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.sportapp.data.settings.ThemeMode

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun SportAppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val color = if (dark) DarkColors else LightColors

    MaterialTheme(
        colorScheme = color,
        typography = Typography,
        content = content
    )
}