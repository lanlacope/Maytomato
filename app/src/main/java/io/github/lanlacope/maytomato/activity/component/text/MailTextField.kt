package io.github.lanlacope.maytomato.activity.component.text

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lanlacope.maytomato.R

@Composable
fun MailTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    MailTextToolbar(
        value = value,
        onValueChange = onValueChange
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                // 全体選択させる
                val newValue = if (value.selection.collapsed && !it.selection.collapsed) {
                    it.copy(selection = TextRange(0, it.text.length))
                } else {
                    it
                }
                onValueChange(newValue)
            },
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
            modifier = modifier
        )
    }
}