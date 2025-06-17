package io.github.lanlacope.maytomato.activity.component

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
import io.github.lanlacope.rewheel.ui.action.setting.SettingTextButton
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.SETTING_MINHEIGHT
import io.github.lanlacope.maytomato.activity.SettingNavi
import io.github.lanlacope.maytomato.clazz.AppTheme
import io.github.lanlacope.maytomato.clazz.rememberThemeManager
import io.github.lanlacope.maytomato.ui.theme.updateTheme
import io.github.lanlacope.rewheel.util.collection.toMutableStateMap
import kotlinx.coroutines.launch

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
            text = stringResource(id = R.string.setting_copipe),
            onClick = { navController.navigate(SettingNavi.COPIPE.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        SettingTextButton(
            text = stringResource(id = R.string.setting_board),
            onClick = { navController.navigate(SettingNavi.BOARD.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        SettingTextButton(
            text = stringResource(id = R.string.setting_image),
            onClick = { navController.navigate(SettingNavi.IMAGE.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        SettingTextButton(
            text = stringResource(id = R.string.setting_about),
            onClick = { navController.navigate(SettingNavi.ABOUT.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )
    }
}