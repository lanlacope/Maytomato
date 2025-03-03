package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lanlacope.compose.composeable.ui.click.BoxButton
import io.github.lanlacope.compose.ui.button.combined.CombinedBoxButton
import io.github.lanlacope.compose.ui.dialog.BasicDialog
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.compose.unit.rememberCacheable
import io.github.lanlacope.compose.unit.toSp
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.BbsInfo
import io.github.lanlacope.maytomato.activity.ChmateString
import io.github.lanlacope.maytomato.activity.rememberCopipeSelectResult
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.rememberBbsPostClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val bbsPoster = rememberBbsPostClient(bbsInfo = bbsInfo, bbsSetting = boardSetting)

    GrowDialog(
        expanded = true,
        onDismissRequest = { activity.finish() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.height(8.dp))

            val subjectFocusRequester = remember { FocusRequester() }
            val messageFocusRequester = remember { FocusRequester() }

            var name by rememberCacheable(key = "${bbsInfo.bbs}_mame"){ mutableStateOf("") }
            var mail by rememberCacheable(key = "${bbsInfo.bbs}_mail") { mutableStateOf("") }
            var subject by rememberCacheable(key = "${bbsInfo.bbs}_subject") { mutableStateOf("") }
            var message by rememberCacheable(key = "${bbsInfo.bbs}_${bbsInfo.key}_message") { mutableStateOf("") }

            LaunchedEffect(Unit) {
                // いい感じに改行して追加
                if (defaultSubject.isNotEmpty()) {
                    subject = defaultSubject
                }
                if (bbsInfo.key.isNullOrEmpty()) {
                    if (defaultMessage.isNotEmpty()) message = defaultMessage
                }
                else {
                    if (defaultMessage.isNotEmpty()) {
                        message = if (message.isEmpty() || message.last() == '\n') {
                            "$message$defaultMessage"
                        } else {
                            "$message\n$defaultMessage"
                        }
                    }
                }

                // いい感じにフォーカス
                if (bbsInfo.key.isNullOrEmpty()) {
                    if (defaultSubject.isNotEmpty()) {
                        messageFocusRequester.requestFocus()
                    } else {
                        subjectFocusRequester.requestFocus()
                    }
                } else {
                    messageFocusRequester.requestFocus()
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
                        .focusRequester(subjectFocusRequester)
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
                        modifier = Modifier
                            .wrapContentSize()
                    )
                },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
                    .focusRequester(messageFocusRequester)
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
                    onClick = { copipeSelectResult.launch() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_text_snippet_24),
                        contentDescription = "Text",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 8.dp)
                    )
                }

                BoxButton(
                    contentAlignment = Alignment.Center,
                    onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            addCategory(Intent.CATEGORY_DEFAULT)
                            data = Uri.parse("https://imgur.com/upload")
                        }
                        activity.startActivity(intent)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "Image",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1.0f))

                var errorDialogTitle by rememberSaveable{ mutableStateOf("") }
                var errorDialogText by rememberSaveable{ mutableStateOf("") }
                var eroorDialogShown by rememberSaveable{ mutableStateOf(false) }
                var waitingDialogShown by rememberSaveable { mutableStateOf(false) }
                val sendPost: () -> Unit = {
                    scope.launch {
                        waitingDialogShown = true
                        bbsPoster.sendPost(
                            name = name,
                            mail = mail,
                            subject = subject,
                            message = message,
                            onSucces = { resNumber ->
                                if (!waitingDialogShown) return@sendPost
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
                                    subject = ""
                                    message = ""
                                    waitingDialogShown = false
                                    activity.setResult(Activity.RESULT_OK)
                                    activity.finish()
                                }
                            },
                            onFailed = { newTitle, newText ->
                                if (!waitingDialogShown) return@sendPost
                                waitingDialogShown = false
                                errorDialogTitle = newTitle
                                errorDialogText = newText
                                eroorDialogShown = true
                            }
                        )
                    }
                }

                CombinedBoxButton(
                    onClick =  sendPost,
                    onLongClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SENDTO
                            addCategory(Intent.CATEGORY_DEFAULT)
                            data = bbsInfo.toUri()
                            component = ComponentName(
                                ChmateString.APP_NAME,
                                ChmateString.ACTIVITY_NAME
                            )
                        }
                        activity.startActivity(intent)
                        activity.finish()
                    },
                    contentAlignment = Alignment.Center,
                    innerPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_positive_post),
                        fontSize = 24.dp.toSp()
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
                    onDismissRequest = { waitingDialogShown = false }
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
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(160.dp)
        ) {

            Text(
                text = stringResource(id = R.string.dialog_waiting_text),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(8.dp))

            CircularProgressIndicator(
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}