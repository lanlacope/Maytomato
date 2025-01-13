package io.github.lanlacope.maytomato.activity.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.compose.ui.text.input.OutlinedInputTextField
import io.github.lanlacope.maytomato.R

@Composable
fun CopipeAddDialog(
    expanded: Boolean,
    onConfirm: (title: String, text: String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
) {
    var title by rememberSaveable(expanded) { mutableStateOf("") }
    var text by rememberSaveable(expanded) { mutableStateOf("") }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_copipe_add),
        expanded = expanded,
        onConfirm = { onConfirm(title, text) },
        confirmText = stringResource(id = R.string.dialog_positive_add),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = title,
                onTextChange = { title = it },
                hintText = stringResource(id = R.string.hint_copipe_title),
                isError = isError,
                errorText = stringResource(id = R.string.dialog_input_warning_exists),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            OutlinedInputTextField(
                text = text,
                onTextChange = { text = it },
                hintText = stringResource(id = R.string.hint_copipe_text),
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
            )
        }
    }
}