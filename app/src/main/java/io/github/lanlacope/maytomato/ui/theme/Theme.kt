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
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.maytomato.clazz.ThemeManager
import io.github.lanlacope.maytomato.clazz.propaty.ThemeJsonPropaty

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

private val updateThemeKey: MutableState<Int> = mutableStateOf(0)

fun updateTheme() {
    updateThemeKey.value++
}

@Composable
fun MaytomatoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val themeManager = ThemeManager(LocalContext.current)

    var theme by remember { mutableStateOf(ThemeJsonPropaty.THEME_SYSTEM) }

    LaunchedEffect(Unit) {
        theme = themeManager.getAppTheme()
    }


    LaunchedEffect(updateThemeKey.value) {
        theme = themeManager.getAppTheme()
    }


    val colorScheme = when (theme) {
        ThemeJsonPropaty.THEME_LIGHT -> LightColorScheme
        ThemeJsonPropaty.THEME_DARK -> DarkColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
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