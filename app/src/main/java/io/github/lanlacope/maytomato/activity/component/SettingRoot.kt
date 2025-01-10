package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import io.github.lanlacope.collection.collection.toMutableStateMap
import io.github.lanlacope.compose.ui.action.setting.SettingTextButton
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.SETTING_MINHEIGHT
import io.github.lanlacope.maytomato.activity.SettingNavi
import io.github.lanlacope.maytomato.clazz.AppTheme
import io.github.lanlacope.maytomato.clazz.rememberThemeManager
import io.github.lanlacope.maytomato.ui.theme.updateTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

/*
 * 設定の一覧を
 * その他の基本設定を提供する
 */

@Composable
fun SettingRoot(navController: NavHostController) {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {

        val scope = rememberCoroutineScope()
        val themeManager = rememberThemeManager()

        var themeSelectDialogShown by remember { mutableStateOf(false) }
        var selectedTheme by remember { mutableStateOf(AppTheme.SYSTEM) }

        LaunchedEffect(Unit) {
            selectedTheme = themeManager.getAppTheme()
        }

        val themes = remember {
            AppTheme.entries.associateWith { theme ->
                theme.getStringResource(context)
            }.toMutableStateMap()
        }

        SettingTextButton(
            text = stringResource(id = R.string.setting_theme),
            value = themes[selectedTheme]!!,
            onClick = { themeSelectDialogShown = true },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        ThemeSelectDialog(
            expanded = themeSelectDialogShown,
            selectedTheme = selectedTheme,
            themes = themes,
            onConfirm = { newTheme ->
                scope.launch {
                    selectedTheme = newTheme
                    themeManager.changeAppTheme(newTheme)
                    updateTheme()
                    themeSelectDialogShown = false
                }
            },
            onCancel = { themeSelectDialogShown = false }
        )

        SettingTextButton(
            text = stringResource(id = R.string.setting_about),
            onClick = { navController.navigate(SettingNavi.ABOUT) },
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