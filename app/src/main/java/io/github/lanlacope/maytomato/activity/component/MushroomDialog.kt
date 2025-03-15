package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.rewheel.composeable.ui.click.BoxButton
import io.github.lanlacope.rewheel.ui.dialog.GrowDialog
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.text.MessageTextField
import io.github.lanlacope.maytomato.activity.rememberCopipeSelectResult
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MushroomDialog() {

    val context = LocalContext.current
    val activity = context as Activity

    GrowDialog(
        expanded = true,
        onDismissRequest = { activity.finish() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.height(8.dp))

            val focusRequester = remember { FocusRequester() }
            var text by rememberSaveable { mutableStateOf("") }

            MessageTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
                    .focusRequester(focusRequester)
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(50.dp)
            ) {
                val copipeSelectResult = rememberCopipeSelectResult { copipe ->
                    // いい感じに改行
                    text = if (text.isEmpty() || text.last() == '\n') "$text$copipe" else "$text\n$copipe"
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

                BoxButton(
                    onClick = {
                        val intent = Intent().apply {
                            putExtra(Simeji.REPLACE_KEY, text.toChmateEscaped())
                        }
                        activity.setResult(Activity.RESULT_OK, intent)
                        activity.finish()
                    },
                    contentAlignment = Alignment.Center,
                    innerPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_positive_convert),
                        fontSize = 24.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private object Simeji {
    const val REPLACE_KEY = "replace_key"
}

/*
 * chmateで文字化けする文字の一覧（抜けあり）
 */
private val mojibakeList = persistentListOf(
    0x000AD..0x000AD,
    0x0034F..0x0034F,
    0x0061C..0x0061C,
    0x017B4..0x017B5,
    0x0180B..0x0180F,
    0x0200B..0x0200F,
    0x0202A..0x0202E,
    0x02060..0x0206F,
    0x0FE00..0x0FE0F,
    0x1D173..0x1D17A,
    0xE0100..0xE01EF
)

private fun isMojibake(codePoint: Int): Boolean {
    return mojibakeList.any { codePoint in it }
}

private fun String.toChmateEscaped(): String {
    return buildString {
        var index = 0
        while (index < this@toChmateEscaped.length) {
            val codePoint = this@toChmateEscaped.codePointAt(index)
            appendForChmate(codePoint)
            // サロゲートペアの場合は下位文字の分も進める
            index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
        }
    }
}

private fun StringBuilder.appendForChmate(
    codePoint: Int
) {
    if (isMojibake(codePoint)) append("&#").append(codePoint.toString(10)).append(";")
    else appendCodePoint(codePoint)
}