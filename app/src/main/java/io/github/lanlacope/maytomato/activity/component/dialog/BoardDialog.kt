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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.action.option.CompactOptionCheckBox
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.compose.ui.text.input.OutlinedInputTextField
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.PostQuery
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun BoardAddDialog(
    expanded: Boolean,
    onConfirm: (boardSettig: BoardSetting) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
) {

    var dDomain by rememberSaveable(expanded) { mutableStateOf("") }
    var dEnabled by rememberSaveable(expanded) { mutableStateOf(true) }
    var dUserAgent by rememberSaveable(expanded) { mutableStateOf("") }
    var dFormOrder = remember(expanded) { emptyList<PostQuery>().toMutableStateList()}
    var dUsedMobileCommunication by rememberSaveable(expanded) { mutableStateOf(false) }
    var dUnusedClearTraffic by rememberSaveable(expanded) { mutableStateOf(false) }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_aa_add),
        expanded = expanded,
        onConfirm = { onConfirm(BoardSetting(dDomain, dEnabled, dUserAgent, dFormOrder.toPersistentList(), dUsedMobileCommunication, dUnusedClearTraffic)) },
        confirmText = stringResource(id = R.string.dialog_positive_add),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = dDomain,
                onTextChange = { dDomain = it },
                hintText = stringResource(id = R.string.hint_aa_title),
                isError = isError,
                errorText = stringResource(id = R.string.dialog_input_warning_exists),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "有効",
                checked = dEnabled,
                onClick = { dEnabled = !dEnabled },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "モバイルで(未実装)",
                checked = dUsedMobileCommunication,
                onClick = { dUsedMobileCommunication = !dUsedMobileCommunication },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "httpsにしない(見自走)",
                checked = dUnusedClearTraffic,
                onClick = { dUnusedClearTraffic = !dUnusedClearTraffic },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            OutlinedInputTextField(
                text = dUserAgent,
                onTextChange = { dUserAgent = it },
                hintText = stringResource(id = R.string.hint_aa_text),
                singleLine = true,
                useLabel = true,
                textStyle = TextStyle.Default.copy(
                    fontFamily = FontFamily(Font(resId = R.font.mona))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .horizontalScroll(rememberScrollState())

            )
        }
    }
}

@Composable
fun BoardEditDialog(
    expanded: Boolean,
    boardSettig: BoardSetting,
    onConfirm: (boardSettig: BoardSetting) -> Unit,
    onCancel: () -> Unit,
) {
    var dDomain by rememberSaveable(expanded) { mutableStateOf(boardSettig.domain) }
    var dEnabled by rememberSaveable(expanded) { mutableStateOf(boardSettig.enabled) }
    var dUserAgent by rememberSaveable(expanded) { mutableStateOf(boardSettig.useAgent) }
    var dFormOrder = remember(expanded) { boardSettig.formOrder.toMutableStateList() }
    var dUsedMobileCommunication by rememberSaveable(expanded) { mutableStateOf(boardSettig.usedMobileCommunication) }
    var dUnusedClearTraffic by rememberSaveable(expanded) { mutableStateOf(boardSettig.unusedClearTraffic) }

    GrowDialog(
        title = stringResource(id = R.string.dialog_title_aa_edit),
        expanded = expanded,
        onConfirm = { onConfirm(BoardSetting(dDomain, dEnabled, dUserAgent, dFormOrder.toPersistentList(), dUsedMobileCommunication, dUnusedClearTraffic)) },
        confirmText = stringResource(id = R.string.dialog_positive_apply),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            OutlinedInputTextField(
                text = dDomain,
                onTextChange = { dDomain = it },
                hintText = stringResource(id = R.string.hint_aa_title),
                singleLine = true,
                useLabel = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "有効",
                checked = dEnabled,
                onClick = { dEnabled = !dEnabled },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "モバイルで(未実装)",
                checked = dUsedMobileCommunication,
                onClick = { dUsedMobileCommunication = !dUsedMobileCommunication },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            CompactOptionCheckBox(
                text = "httpsにしない(見自走)",
                checked = dUnusedClearTraffic,
                onClick = { dUnusedClearTraffic = !dUnusedClearTraffic },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            )

            OutlinedInputTextField(
                text = dUserAgent,
                onTextChange = { dUserAgent = it },
                hintText = stringResource(id = R.string.hint_aa_text),
                singleLine = true,
                useLabel = true,
                textStyle = TextStyle.Default.copy(
                    fontFamily = FontFamily(Font(resId = R.font.mona))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .horizontalScroll(rememberScrollState())

            )
        }
    }
}

@Composable
fun BoardRemoveDialog(
    expanded: Boolean,
    domain: String,
    onConfirm: (title: String) -> Unit,
    onCancel: () -> Unit,
) {
    GrowDialog(
        title = stringResource(id = R.string.dialog_title_comfirm),
        expanded = expanded,
        onConfirm = { onConfirm(domain) },
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