package io.github.lanlacope.maytomato.activity.component.text

import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lanlacope.rewheel.ui.text.selectable.SelectableText

@Composable
fun ResultText(
    value: String,
    modifier: Modifier = Modifier
) {

    var textFieldValue by remember(value) { mutableStateOf(TextFieldValue(text = value)) }

    ResultTextToolbar(
        value = textFieldValue,
        onValueChange = { textFieldValue = it }
    ) {
        SelectableText(
            value = textFieldValue,
            onValueChange = { newValue ->

                val urlResults = Patterns.WEB_URL.toRegex().findAll(newValue.text)

                if (textFieldValue.selection.collapsed && !newValue.selection.collapsed) {
                    urlResults.firstOrNull { result ->
                        newValue.selection.start in result.range && newValue.selection.end in result.range
                    }?.range?.let { range ->
                        // URL全体を選択
                        textFieldValue = newValue.copy(selection = TextRange(range.first, range.last + 1))
                        return@SelectableText
                    }
                }

                textFieldValue = newValue
            },
            modifier = modifier
        )
    }
}