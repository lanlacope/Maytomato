package io.github.lanlacope.maytomato.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.lanlacope.maytomato.clazz.AppTheme
import io.github.lanlacope.maytomato.clazz.rememberThemeManager
import kotlinx.coroutines.runBlocking

private val LightColorScheme = lightColorScheme(
    primary = PaupleDark,
    secondary = BlueDark,
    tertiary = BlueLight,
    background = White,
    onBackground = Black,
    error = RedDark
)

private val DarkColorScheme = darkColorScheme(
    primary = PaupleLight,
    secondary = BlueLight,
    tertiary = BlueDark,
    background = Black,
    onBackground = White,
    error = RedLight
)

private val updateThemeKey: MutableState<Int> = mutableStateOf(0)

fun updateTheme() {
    updateThemeKey.value++
}

@Composable
fun MaytomatoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeManager = rememberThemeManager()

    var theme by remember { mutableStateOf(AppTheme.SYSTEM) }

    LaunchedEffect(Unit) {
        theme = runBlocking { themeManager.getAppTheme() }
    }

    LaunchedEffect(updateThemeKey.value) {
        theme = themeManager.getAppTheme()
    }

    val colorScheme = when (theme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.SYSTEM -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    /*
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
     */

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}