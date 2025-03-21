package io.github.lanlacope.maytomato.activity.component.dialog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.rewheel.ui.dialog.GrowDialog
import io.github.lanlacope.rewheel.ui.text.input.OutlinedInputTextField
import io.github.lanlacope.maytomato.R

@Composable
fun AAAddDialog(
    expanded: Boolean,
    onConfirm: (title: String, text: String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
) {
    var title by rememberSaveable(expanded) { mutableStateOf("") }
    var text by rememberSaveable(expanded) { mutableStateOf("") }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_aa_add),
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
                hintText = stringResource(id = R.string.hint_aa_title),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            OutlinedInputTextField(
                text = text,
                onTextChange = { text = it },
                isError = isError,
                errorText = stringResource(id = R.string.dialog_input_warning_exists),
                hintText = stringResource(id = R.string.hint_aa_text),
                useLabel = true,
                textStyle = TextStyle.Default.copy(
                    fontFamily = FontFamily(Font(resId = R.font.mona))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
                    .horizontalScroll(rememberScrollState())

            )
        }
    }
}

@Composable
fun AAEditDialog(
    expanded: Boolean,
    title: String,
    text: String,
    onConfirm: (title: String, text: String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean
) {
    var dTitle by rememberSaveable(expanded) { mutableStateOf(title) }
    var dText by rememberSaveable(expanded) { mutableStateOf(text) }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_aa_edit),
        expanded = expanded,
        onConfirm = { onConfirm(dTitle, dText) },
        confirmText = stringResource(id = R.string.dialog_positive_apply),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = dTitle,
                onTextChange = { dTitle = it },
                hintText = stringResource(id = R.string.hint_aa_title),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            OutlinedInputTextField(
                text = dText,
                onTextChange = { dText = it },
                isError = isError,
                errorText = stringResource(id = R.string.dialog_input_warning_exists),
                hintText = stringResource(id = R.string.hint_aa_text),
                useLabel = true,
                textStyle = TextStyle.Default.copy(
                    fontFamily = FontFamily(Font(resId = R.font.mona))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
                    .horizontalScroll(rememberScrollState())

            )
        }
    }
}

@Composable
fun AARemoveDialog(
    expanded: Boolean,
    title: String,
    onConfirm: (title: String) -> Unit,
    onCancel: () -> Unit,
) {
    GrowDialog(
        title = stringResource(id = R.string.dialog_title_comfirm),
        expanded = expanded,
        onConfirm = { onConfirm(title) },
        confirmText = stringResource(id = R.string.dialog_positive_remove),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = stringResource(id = R.string.confirm_text_aa_remove),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)

            )
        }
    }
}