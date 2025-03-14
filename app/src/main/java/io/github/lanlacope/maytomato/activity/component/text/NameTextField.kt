package io.github.lanlacope.maytomato.activity.component.text

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lanlacope.maytomato.R

@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember(value) { mutableStateOf(TextFieldValue(text = value)) }

    NameTextToolbar(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onValueChange(it.text)
        }    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                onValueChange(it.text)
            },
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
            modifier = modifier
        )
    }
}