package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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

    private val appContext = context.applicationContext

    suspend fun getCookie(domain: String): String = withContext(Dispatchers.IO) {
        return@withContext appContext.cookieDataStore.data.map { cookie ->
            cookie[stringPreferencesKey(domain)] ?: ""
        }.first()
    }

    suspend fun updateCookie(domain: String, cookie: String) = withContext(Dispatchers.IO) {
        appContext.cookieDataStore.edit { data ->
            data[stringPreferencesKey(domain)] = cookie
        }
    }

    suspend fun removeCookie(domain: String) = withContext(Dispatchers.IO) {
        appContext.cookieDataStore.edit { data ->
            data.remove(stringPreferencesKey(domain))
        }
    }
}