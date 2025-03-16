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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.lanlacope.rewheel.composeable.ui.click.BoxButton
import io.github.lanlacope.rewheel.ui.button.combined.CombinedBoxButton
import io.github.lanlacope.rewheel.ui.dialog.GrowDialog
import io.github.lanlacope.rewheel.util.rememberCacheable
import io.github.lanlacope.rewheel.util.toSp
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.BbsInfo
import io.github.lanlacope.maytomato.activity.ChmateString
import io.github.lanlacope.maytomato.activity.component.dialog.ErrorDialog
import io.github.lanlacope.maytomato.activity.component.dialog.WaitingDialog
import io.github.lanlacope.maytomato.activity.component.text.MailTextField
import io.github.lanlacope.maytomato.activity.component.text.MessageTextField
import io.github.lanlacope.maytomato.activity.component.text.NameTextField
import io.github.lanlacope.maytomato.activity.component.text.SubjectTextField
import io.github.lanlacope.maytomato.activity.rememberCopipeSelectResult
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.appendText
import io.github.lanlacope.maytomato.clazz.isBreakLast
import io.github.lanlacope.maytomato.clazz.isSelectedPrefix
import io.github.lanlacope.maytomato.clazz.rememberBbsPostClient
import io.github.lanlacope.rewheel.util.insertText
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

            var cashedName by rememberCacheable(key = "${bbsInfo.bbs}_name"){ mutableStateOf("") }
            var cashedMail by rememberCacheable(key = "${bbsInfo.bbs}_mail") { mutableStateOf("") }
            var cashedSubject by rememberCacheable(key = "${bbsInfo.bbs}_subject") { mutableStateOf("") }
            var cashedMessage by rememberCacheable(key = "${bbsInfo.bbs}_${bbsInfo.key}_message") { mutableStateOf("") }

            var name by remember { mutableStateOf(TextFieldValue(cashedName)) }
            var mail by remember { mutableStateOf(TextFieldValue(cashedMail)) }
            var subject by remember { mutableStateOf(TextFieldValue(cashedSubject)) }
            var message by remember { mutableStateOf(TextFieldValue(cashedMessage)) }

            // キャッシュに反映
            LaunchedEffect(name.text, mail.text, subject.text, message.text) {
                cashedName = name.text
                cashedMail = mail.text
                cashedSubject = subject.text
                cashedMessage = message.text
            }

            LaunchedEffect(Unit) {
                // いい感じに改行して追加
                if (bbsInfo.key.isNullOrEmpty()) {
                    if (defaultSubject.isNotEmpty()) subject = TextFieldValue(text = defaultSubject)
                    if (defaultMessage.isNotEmpty()) message =TextFieldValue(text = defaultMessage)
                }
                else {
                    if (defaultMessage.isNotEmpty()) {
                        message = if (message.text.isEmpty() || message.text.isBreakLast()) {
                            message.appendText(defaultMessage)
                        } else {
                            message.appendText("\n$defaultMessage")
                        }
                    }
                }

                // カーソルを最後に
                name = name.copy(selection = TextRange(name.text.length))
                mail = mail.copy(selection = TextRange(mail.text.length))
                subject = subject.copy(selection = TextRange(subject.text.length))
                message = message.copy(selection = TextRange(message.text.length))

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
                NameTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f)
                        .padding(all = 8.dp)
                )

                if (!boardSetting.removeMail) {
                    MailTextField(
                        value = mail,
                        onValueChange = { mail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f)
                            .padding(all = 8.dp)

                    )
                }
            }

            if (bbsInfo.key.isNullOrEmpty()) {
                SubjectTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                        .focusRequester(subjectFocusRequester)
                )
            }

            MessageTextField(
                value = message,
                onValueChange = { message = it },
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
                    message = if (message.isSelectedPrefix()) {
                        message.insertText(AnnotatedString("$copipe\n"))
                    } else {
                        message.insertText(AnnotatedString("\n$copipe\n"))
                    }
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
                            name = name.text,
                            mail = mail.text,
                            subject = subject.text,
                            message = message.text,
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
                                    // キャッシュを削除
                                    cashedSubject = ""
                                    cashedMessage = ""

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