package io.github.lanlacope.maytomato.clazz.propaty

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun versionName(): String? {
    val activity = LocalContext.current as Activity
    val name = activity.getPackageName()

    val pm: PackageManager = activity.getPackageManager()

    val info = pm.getPackageInfo(name, PackageManager.GET_META_DATA)

    return info.versionName
}

suspend fun getLatestVersion(): String? = withContext(Dispatchers.Default) {
    try {
        val response = URL(AppGitHost.LATEST_API).readText()
        println(response)
        val jsonObject = JSONObject(response)
        return@withContext jsonObject.getString(AppGitHost.LATEST_TAG)
    } catch (e: Exception) {
        null
    }
}

object Simeji {
    const val REPLACE_KEY = "replace_key"
}

