package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.composeable.ui.click.BoxButton
import io.github.lanlacope.compose.ui.dialog.BasicDialog
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.compose.unit.rememberCacheable
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.BbsInfo
import io.github.lanlacope.maytomato.activity.rememberCopipeSelectResult
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.rememberBbsPoster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.security.auth.Subject

@Composable
fun WriteDialog(
    defaultSubject: String,
    defaultMessage: String,
    bbsInfo: BbsInfo,
    boardSetting: BoardSetting
) {

    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()
    val bbsPoster = rememberBbsPoster(bbsInfo = bbsInfo, bbsSetting = boardSetting)

    GrowDialog(
        expanded = true,
        onDismissRequest = { activity.finish() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.height(8.dp))

            var postSuccessKey by remember { mutableStateOf(0) }
            var name by rememberCacheable(key = "${bbsInfo.bbs}_mame"){ mutableStateOf("") }
            var mail by rememberCacheable(key = "${bbsInfo.bbs}_mail") { mutableStateOf("") }
            var subject by rememberCacheable(postSuccessKey, key = "${bbsInfo.bbs}_subject") { mutableStateOf("") }
            var message by rememberCacheable(postSuccessKey, key = "${bbsInfo.bbs}_${bbsInfo.key}_message") { mutableStateOf("") }

            LaunchedEffect(Unit) {
                if (subject.isNotEmpty()) subject = defaultSubject
                if (bbsInfo.key.isNullOrEmpty()) {
                    if (message.isNotEmpty()) message = defaultMessage
                }
                else {
                    if (message.isNotEmpty()) {
                        message = if (message.isEmpty() || message.last() == '\n') "$message$defaultMessage" else "$message\n$defaultMessage"
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.dialog_hint_name),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                color = Gray
                            ),
                            modifier = Modifier.wrapContentSize()
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f)
                        .padding(all = 8.dp)
                )

                if (!boardSetting.removeMail) {
                    OutlinedTextField(
                        value = mail,
                        onValueChange = { mail = it },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.dialog_hint_mail),
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    color = Gray
                                ),
                                modifier = Modifier.wrapContentSize()
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f)
                            .padding(all = 8.dp)
                    )
                }
            }

            if (bbsInfo.key.isNullOrEmpty()) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.dialog_hint_subject),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                color = Gray
                            ),
                            modifier = Modifier.wrapContentSize()
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                )
            }

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.dialog_hint_message),
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            color = Gray
                        ),
                        modifier = Modifier.wrapContentSize()
                    )
                },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(50.dp)
            ) {
                val copipeSelectResult = rememberCopipeSelectResult { copipe ->
                    // いい感じに改行
                    message = if (message.isEmpty() || message.last() == '\n') "$message$copipe" else "$message\n$copipe"
                }

                BoxButton(
                    contentAlignment = Alignment.Center,
                    onClick = {
                        copipeSelectResult.launch()
                    },
                    innerPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.setting_copipe),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.weight(1.0f))

                var errorDialogTitle by rememberSaveable{ mutableStateOf("") }
                var errorDialogText by rememberSaveable{ mutableStateOf("") }
                var eroorDialogShown by rememberSaveable{ mutableStateOf(false) }
                var waitingDialogShown by rememberSaveable { mutableStateOf(false) }
                val sendPost: () -> Unit = {
                    scope.launch {
                        bbsPoster.sendPost(
                            name = name,
                            mail = mail,
                            subject = subject,
                            message = message,
                            onSucces = { resNumber ->
                                scope.launch(Dispatchers.Main) {
                                    if (resNumber == null) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.toast_state_success_post),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.toast_state_success_post_with_num, resNumber),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    waitingDialogShown = false
                                    postSuccessKey++
                                    activity.setResult(Activity.RESULT_OK)
                                    activity.finish()
                                }
                            },
                            onFailed = { newTitle, newText ->
                                waitingDialogShown = false
                                errorDialogTitle = newTitle
                                errorDialogText = newText
                                eroorDialogShown = true
                            }
                        )
                    }
                }

                TextButton(
                    onClick =  sendPost,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_positive_post),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                ErrorDialog(
                    expanded = eroorDialogShown,
                    title = errorDialogTitle,
                    text = errorDialogText,
                    onConfirm = {
                        eroorDialogShown = false
                        sendPost()
                    },
                    onCancel = { eroorDialogShown = false }

                )

                WaitingDialog(
                    expanded = waitingDialogShown,
                    onDismissRequest = { }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ErrorDialog(
    expanded: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    BasicDialog(
        title = title,
        expanded = expanded,
        onConfirm = onConfirm,
        confirmText = stringResource(id = R.string.dialog_positive_retry),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column {
            SelectionContainer {
                Text(
                    text = text,
                )
            }
        }
    }
}

@Composable
private fun WaitingDialog(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    BasicDialog(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(200.dp)
        )
    }
}