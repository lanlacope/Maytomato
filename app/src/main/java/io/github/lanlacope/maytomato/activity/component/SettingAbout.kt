package io.github.lanlacope.maytomato.activity.component

import android.content.Intent
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