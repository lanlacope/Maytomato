package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.lanlacope.maytomato.clazz.propaty.ThemeJsonPropaty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("unused")
@Composable
fun rememberThemeManager(): ThemeManager {
    val context = LocalContext.current
    return remember {
        ThemeManager(context)
    }
}

/*
 * アプリテーマの管理を行う
 *
 */
@Stable
class ThemeManager(private val context: Context) {

    private val THEME_KEY = stringPreferencesKey("theme_type")
    private val Context.dataStore by preferencesDataStore(name = "theme")

    private val themeFile by lazy {
        val file = File(context.filesDir, "theme.json")
        file.createNewFile()
        file
    }

    suspend fun getAppTheme(): String = withContext(Dispatchers.IO) {
        return@withContext context.dataStore.data.map { data ->
            data[THEME_KEY] ?: ThemeJsonPropaty.THEME_SYSTEM
        }.first()
    }

    suspend fun changeAppTheme(theme: String) = withContext(Dispatchers.IO) {
        context.dataStore.edit { data ->
            data[THEME_KEY] = theme
        }
    }
}