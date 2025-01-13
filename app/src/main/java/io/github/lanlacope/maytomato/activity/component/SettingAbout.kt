package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.lanlacope.compose.ui.action.setting.SettingTextButton
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.SETTING_MINHEIGHT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

/*
 * このアプリの情報を表示
 */

@Composable
fun SettingAbout() {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {

        SettingTextButton(
            text = stringResource(id = R.string.setting_about_source),
            onClick = {
                val uri = Uri.parse(AppGitHost.SOURCE)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        SettingTextButton(
            text = stringResource(id = R.string.setting_about_licence),
            onClick = {
                val uri = Uri.parse(AppGitHost.LICENSE)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        val appVersion = versionName()
        var latestText: String? by remember { mutableStateOf(null) }

        LaunchedEffect(Unit) {
            val latestVersion = getLatestVersion()
            if (!latestVersion.isNullOrEmpty() && appVersion != latestVersion) {
                latestText = context.getString(R.string.setting_about_version_update)
            }
        }

        SettingTextButton(
            text = stringResource(id = R.string.setting_about_version),
            value = appVersion,
            summary = latestText,
            onClick = {
                val uri = Uri.parse(AppGitHost.LATEST)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )
    }

}

object AppGitHost {
    const val SOURCE: String = "https://github.com/lanlacope/Maytomato"
    const val LICENSE: String = "https://github.com/lanlacope/Maytomato/blob/master/README.MD#license"
    const val LATEST: String = "https://github.com/lanlacope/Maytomato/releases/latest"
    const val LATEST_API: String = "https://api.github.com/repos/Maytomato/NXShare/releases/latest"
    const val LATEST_TAG: String = "tag_name"
}

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