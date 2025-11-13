package com.example.appsneakerstore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = DarkGray,
    onSecondary = White,
    background = White,
    surface = Gray,
    onBackground = Black,
    onSurface = Black,
    primaryContainer = Accent,
    onPrimaryContainer = White
)

@Composable
fun AppSneakerStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme, // Forzamos el tema claro por ahora
        typography = Typography,
        content = content
    )
}
