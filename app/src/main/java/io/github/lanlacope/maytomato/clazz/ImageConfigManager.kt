package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.rewheel.util.json.forEach
import io.github.lanlacope.rewheel.util.json.getStringOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

@Suppress("unused")
@Composable
fun rememberImageConfigManager(): ImageConfigManager {
    val context = LocalContext.current
    return remember {
        ImageConfigManager(context)
    }
}

class ImageConfigManager(context: Context) {

    val appContext = context.applicationContext

    suspend fun getUrl(): String = withContext(Dispatchers.IO) {
        val configObject = try {
            JSONObject(appContext.getImageConfigFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val url = configObject.getStringOrNull(ImageJson.URL) ?: ""

        return@withContext url
    }

    suspend fun editUrl(url: String) = withContext(Dispatchers.IO) {

        val configFile = appContext.getImageConfigFile()

        val configObject = try {
            JSONObject(configFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        configObject.put(ImageJson.URL, url)
        configFile.writeText(configObject.toString())

        return@withContext
    }
}

private object ImageJson {
    const val URL = "url"
}