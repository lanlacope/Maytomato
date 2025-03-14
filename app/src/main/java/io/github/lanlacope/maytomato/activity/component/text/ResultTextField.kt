package io.github.lanlacope.maytomato.activity.component.text

import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lanlacope.rewheel.ui.text.selectable.SelectableText

@Composable
fun ResultTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {

    ResultTextToolbar(
        value = value,
        onValueChange = onValueChange
    ) {
        SelectableText(
            value = value,
            onValueChange = { newValue ->

                val urlResults = Patterns.WEB_URL.toRegex().findAll(value.text)

                if (!newValue.selection.collapsed) {
                    val matchedUrl = urlResults.find { result ->
                        newValue.selection.start in result.range && newValue.selection.end in result.range
                    }

                    if (matchedUrl != null) {
                        // URL全体を選択するように調整
                        val urlSelection =
                            TextRange(matchedUrl.range.first, matchedUrl.range.last + 1)
                        onValueChange(newValue.copy(selection = urlSelection))
                        return@SelectableText
                    }
                }
                onValueChange(newValue)
            },
            modifier = modifier
        )
    }
}