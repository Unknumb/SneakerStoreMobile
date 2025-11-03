package com.example.appsneakerstore.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

import com.example.appsneakerstore.ui.theme.GreenLight
import com.example.appsneakerstore.ui.theme.GreenMedium1
import com.example.appsneakerstore.ui.theme.GreenMedium2
import com.example.appsneakerstore.ui.theme.GreenDark1
import com.example.appsneakerstore.ui.theme.GreenDark2


private val DarkColorScheme = darkColorScheme(
    primary = GreenDark2,
    secondary = GreenDark1,
    tertiary = GreenMedium2,
    background = GreenDark1,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = GreenLight
)

private val LightColorScheme = lightColorScheme(
    primary = GreenDark1,
    onPrimary = Color.White,
    secondary = GreenMedium1,
    onSecondary = Color.White,
    tertiary = GreenMedium2,
    onTertiary = Color.White,
    background = GreenLight,
    surface = Color.White,
    onBackground = GreenDark2,
    onSurface = GreenDark2
)

@Composable
fun AppSneakerStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivamos colores dinámicos para forzar paleta personalizada
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Omitimos usar colores dinámicos. Siempre usar la paleta fija personalizada.
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
