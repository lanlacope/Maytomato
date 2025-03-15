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
import io.github.lanlacope.maytomato.clazz.forChmateEscape

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
                            putExtra(Simeji.REPLACE_KEY, text.forChmateEscape())
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