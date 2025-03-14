package io.github.lanlacope.maytomato.activity.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.text.ResultText
import io.github.lanlacope.rewheel.ui.dialog.BasicDialog


@Composable
fun ErrorDialog(
    expanded: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    BasicDialog(
        title = title,
        expanded = expanded,
        onConfirm = onConfirm,
        confirmText = stringResource(id = R.string.dialog_positive_retry),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column {
            ResultText(
                value = text,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WaitingDialog(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    BasicDialog(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(160.dp)
        ) {

            Text(
                text = stringResource(id = R.string.dialog_waiting_text),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(8.dp))

            CircularProgressIndicator(
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}