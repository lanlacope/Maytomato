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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.lanlacope.rewheel.ui.action.setting.SettingTextButton
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.SETTING_MINHEIGHT
import io.github.lanlacope.maytomato.activity.component.dialog.ImageUrlEditDialog
import io.github.lanlacope.maytomato.clazz.rememberImageConfigManager
import kotlinx.coroutines.launch

/*
 * このアプリの情報を表示
 */

@Composable
fun SettingImage() {

    Column(modifier = Modifier.fillMaxSize()) {

        val scope = rememberCoroutineScope()
        val imageConfigManager = rememberImageConfigManager()

        var imageUrlEditDialogShown by rememberSaveable { mutableStateOf(false) }

        var imageUrl by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            imageUrl = imageConfigManager.getUrl()
        }

        SettingTextButton(
            text = stringResource(id = R.string.setting_image_url),
            value = imageUrl,
            onClick = {
                imageUrlEditDialogShown = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SETTING_MINHEIGHT)
        )

        ImageUrlEditDialog(
            expanded = imageUrlEditDialogShown,
            url = imageUrl,
            onConfirm = { newUrl ->
                scope.launch {
                    imageConfigManager.editUrl(newUrl)
                    imageUrl = newUrl
                    imageUrlEditDialogShown = false
                }
            },
            onCancel = { imageUrlEditDialogShown = false }
        )


    }
}