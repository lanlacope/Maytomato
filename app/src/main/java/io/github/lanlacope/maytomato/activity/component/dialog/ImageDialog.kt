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
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.rewheel.ui.dialog.GrowDialog
import io.github.lanlacope.rewheel.ui.text.input.OutlinedInputTextField

@Composable
fun ImageUrlEditDialog(
    expanded: Boolean,
    url: String,
    onConfirm: (url: String) -> Unit,
    onCancel: () -> Unit
) {
    var dUrl by rememberSaveable(expanded) { mutableStateOf(url) }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_copipe_edit),
        expanded = expanded,
        onConfirm = { onConfirm(dUrl) },
        confirmText = stringResource(id = R.string.dialog_positive_apply),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = dUrl,
                onTextChange = { dUrl = it },
                hintText = stringResource(id = R.string.hint_image_url),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )
        }
    }
}