package io.github.lanlacope.maytomato.activity.component.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.clazz.convertDecimalSelectedText
import io.github.lanlacope.maytomato.clazz.convertEntitySelectedText
import io.github.lanlacope.maytomato.clazz.convertHexadecimalSelectedText
import io.github.lanlacope.rewheel.ui.text.toolbar.rememberDynamicTextToolbar
import io.github.lanlacope.rewheel.util.insertText
import io.github.lanlacope.rewheel.util.isNotSelectedAll
import io.github.lanlacope.rewheel.util.isNotSelectedNone
import io.github.lanlacope.rewheel.util.removeSelectedText
import io.github.lanlacope.rewheel.util.selectAll

@Composable
fun SubjectTextToolbar(
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
                action = { onValueChange(value.convertDecimalSelectedText()) }
            )

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_convert_hexadecimal),
                action = { onValueChange(value.convertHexadecimalSelectedText()) }
            )

            textToolbar.addAction(
                title = context.getString(R.string.toolbar_title_convert_entity),
                action = { onValueChange(value.convertEntitySelectedText()) }
            )
        }
    }

    CompositionLocalProvider(
        LocalTextToolbar provides textToolbar,
        content = content
    )
}