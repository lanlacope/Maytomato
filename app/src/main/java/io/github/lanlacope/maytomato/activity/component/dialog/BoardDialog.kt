package io.github.lanlacope.maytomato.activity.component.dialog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.rewheel.ui.action.setting.SettingSwitch
import io.github.lanlacope.rewheel.ui.action.setting.SettingTextButton
import io.github.lanlacope.rewheel.ui.action.setting.SettingTextButtonDefaults
import io.github.lanlacope.rewheel.ui.dialog.GrowDialog
import io.github.lanlacope.rewheel.ui.text.input.InputTextField
import io.github.lanlacope.rewheel.ui.text.input.OutlinedInputTextField
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.SETTING_MINHEIGHT
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.rememberCookieManager
import kotlinx.coroutines.launch


@Composable
fun BoardAddDialog(
    expanded: Boolean,
    onConfirm: (domain: String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
) {

    var dDomain by rememberSaveable(expanded) { mutableStateOf("") }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_domain_add),
        expanded = expanded,
        onConfirm = { onConfirm(dDomain) },
        confirmText = stringResource(id = R.string.dialog_positive_add),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = dDomain,
                onTextChange = { dDomain = it },
                hintText = "ドメイン",
                isError = isError,
                errorText = stringResource(id = R.string.dialog_input_warning_exists),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardEditDialog(
    expanded: Boolean,
    boardSetting: BoardSetting,
    onConfirm: (boardSetting: BoardSetting) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val cookieManager = rememberCookieManager()

    var dEnabled by rememberSaveable(expanded) { mutableStateOf(boardSetting.enabled) }
    var dUserAgent by rememberSaveable(expanded) { mutableStateOf(boardSetting.userAgent) }
    var dRemoveMail by rememberSaveable(expanded) { mutableStateOf(boardSetting.removeMail) }
    var dForceMobileCommunication by rememberSaveable(expanded) { mutableStateOf(boardSetting.forceMobileCommunication) }
    var dForceHttp by rememberSaveable(expanded) { mutableStateOf(boardSetting.forceClearHttps) }

    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = {
                onConfirm(
                    BoardSetting(
                        boardSetting.domain,
                        dEnabled,
                        dUserAgent,
                        dRemoveMail,
                        dForceMobileCommunication,
                        dForceHttp
                    )
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {

                Text(
                    text = boardSetting.domain,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )

                SettingSwitch(
                    text = stringResource(id = R.string.setting_board_enabled),
                    checked = dEnabled,
                    onClick = { dEnabled = !dEnabled },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )

                SettingSwitch(
                    text = stringResource(id = R.string.setting_board_remove_mail),
                    checked = dRemoveMail,
                    onClick = { dRemoveMail = !dRemoveMail },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )

                SettingSwitch(
                    text = stringResource(id = R.string.setting_board_force_mobile_communication),
                    checked = dForceMobileCommunication,
                    onClick = { dForceMobileCommunication = !dForceMobileCommunication },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )

                SettingSwitch(
                    text = stringResource(id = R.string.setting_board_force_https),
                    checked = dForceHttp,
                    onClick = { dForceHttp = !dForceHttp },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )

                InputTextField(
                    text = dUserAgent,
                    onTextChange = { dUserAgent = it },
                    hintText = stringResource(id = R.string.hint_board_ua),
                    singleLine = true,
                    useLabel = true,
                    textStyle = TextStyle.Default.copy(
                        fontFamily = FontFamily(Font(resId = R.font.mona))
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                        .horizontalScroll(rememberScrollState())

                )

                var removeDialogShown by rememberSaveable { mutableStateOf(false) }

                SettingTextButton(
                    text = stringResource(id = R.string.setting_board_remove_cookie),
                    colors = SettingTextButtonDefaults.colors().copy(
                        textColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { removeDialogShown = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = SETTING_MINHEIGHT)
                )

                CookieRemoveDialog(
                    expanded = removeDialogShown,
                    onConfirm = { ->
                        scope.launch {
                            cookieManager.removeCookie(boardSetting.domain)
                            removeDialogShown = false
                        }
                    },
                    onCancel = { removeDialogShown = false }
                )
            }
        }
    }
}

@Composable
fun BoardRemoveDialog(
    expanded: Boolean,
    domain: String,
    onConfirm: (title: String) -> Unit,
    onCancel: () -> Unit,
) {
    GrowDialog(
        title = stringResource(id = R.string.dialog_title_comfirm),
        expanded = expanded,
        onConfirm = { onConfirm(domain) },
        confirmText = stringResource(id = R.string.dialog_positive_remove),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = stringResource(id = R.string.confirm_text_domain_remove),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)

            )
        }
    }
}

@Composable
private fun CookieRemoveDialog(
    expanded: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    GrowDialog(
        title = stringResource(id = R.string.dialog_title_comfirm),
        expanded = expanded,
        onConfirm = { onConfirm() },
        confirmText = stringResource(id = R.string.dialog_positive_remove),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = stringResource(id = R.string.confirm_text_cookie_remove),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)

            )
        }
    }
}