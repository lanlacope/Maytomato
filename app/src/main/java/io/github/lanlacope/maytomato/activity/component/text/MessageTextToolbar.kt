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
import io.github.lanlacope.maytomato.clazz.toDecimal
import io.github.lanlacope.maytomato.clazz.toEntity
import io.github.lanlacope.maytomato.clazz.toHexadecimal
import io.github.lanlacope.rewheel.ui.text.toolbar.rememberDynamicTextToolbar
import io.github.lanlacope.rewheel.util.insertText
import io.github.lanlacope.rewheel.util.isNotSelectedAll
import io.github.lanlacope.rewheel.util.isNotSelectedNone
import io.github.lanlacope.rewheel.util.removeSelectedText
import io.github.lanlacope.rewheel.util.selectAll

@Composable
fun MessageTextToolbar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val textToolbar = rememberDynamicTextToolbar()

    LaunchedEffect(value) {
        textToolbar.clearActions()

        if (value.isNotSelectedNone()) {

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_cut),
                action = {
                    val selectedText = value.getSelectedText()
                    clipboardManager.setText(selectedText)
                    onValueChange(value.removeSelectedText())
                }
            )

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_copy),
                action = {
                    val selectedText = value.getSelectedText()
                    clipboardManager.setText(selectedText)
                }
            )
        }

        textToolbar.addAction(
            title = context.getString(R.string.toolbar_title_paste),
            action = {
                val clipText = clipboardManager.getText() ?: return@addAction
                onValueChange(value.insertText(clipText))
            }
        )

        if (value.isNotSelectedAll()) {
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_select_all),
                action = { onValueChange(value.selectAll()) }
            )
        }

        if (value.isNotSelectedNone()) {
            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_convert_decimal),
                action = {
                    val newValue = value.copy(text = value.text.toDecimal())
                    onValueChange(newValue)
                }
            )

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_convert_hexadecimal),
                action = {
                    val newValue = value.copy(text = value.text.toHexadecimal())
                    onValueChange(newValue)
                }
            )

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_convert_entity),
                action = {
                    val newValue = value.copy(text = value.text.toEntity())
                    onValueChange(newValue)
                }
            )
        }

    }

    CompositionLocalProvider(
        LocalTextToolbar provides textToolbar,
        content = content
    )
}