package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore

@Suppress("unused")
@Composable
fun rememberCookieManager(): CookieManager {
    val context = LocalContext.current
    return remember {
        CookieManager(context)
    }
}

private val Context.cookieDataStore by preferencesDataStore(name = "cookie")


class CookieManager(context: Context) {
}