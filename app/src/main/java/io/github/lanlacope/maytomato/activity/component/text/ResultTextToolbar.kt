package io.github.lanlacope.maytomato.activity.component.text

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.rewheel.ui.text.toolbar.rememberDynamicTextToolbar
import io.github.lanlacope.rewheel.util.isNotSelectedAll
import io.github.lanlacope.rewheel.util.isNotSelectedNone
import io.github.lanlacope.rewheel.util.selectAll

@Composable
fun ResultTextToolbar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val textToolbar = rememberDynamicTextToolbar()

    LaunchedEffect(value) {
        textToolbar.clearActions()

        if (Patterns.WEB_URL.toRegex().matches(value.getSelectedText().text)) {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(value.getSelectedText().text)
            }
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_open_in_browser),
                action = { context.startActivity(intent) }
            )
        }

        if (value.isNotSelectedNone()) {
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_copy),
                action = {
                    val selectedText = value.getSelectedText()
                    clipboardManager.setText(selectedText)
                }
            )
        }

        if (value.isNotSelectedAll()) {
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_select_all),
                action = { onValueChange(value.selectAll()) }
            )
        }

        if (value.isNotSelectedNone()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val translateIntent = Intent().apply {
                    action = Intent.ACTION_TRANSLATE
                    putExtra(Intent.EXTRA_TEXT, value.getSelectedText().text)
                }
                textToolbar.addAction(
                    title = context.getString(R.string.toolbar_title_translate),
                    action = { context.startActivity(translateIntent) }
                )
            }

            val searchIntent = Intent().apply {
                action = Intent.ACTION_WEB_SEARCH
                putExtra(SearchManager.QUERY, value.getSelectedText().text)
            }
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_search),
                action = { context.startActivity(searchIntent) }
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, value.getSelectedText().text)
            }
            if (context.packageManager.queryIntentActivities(shareIntent, 0).isNotEmpty()) {
                textToolbar.addAction(
                    title = context.getString(R.string.toolbar_title_share),
                    action = { context.startActivity(shareIntent) }
                )
            }
        }
    }

    CompositionLocalProvider(
        LocalTextToolbar provides textToolbar,
        content = content
    )
}