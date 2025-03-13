package io.github.lanlacope.maytomato.activity.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.lanlacope.rewheel.ui.dialog.option.RadioButtonDialog
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.clazz.AppTheme

@Composable
fun ThemeSelectDialog(
    expanded: Boolean,
    selectedTheme: AppTheme,
    themes: Map<AppTheme, String>,
    onConfirm: (theme: AppTheme) -> Unit,
    onCancel: () -> Unit,
) {
    RadioButtonDialog(
        title = stringResource(id = R.string.dialog_title_theme),
        options = themes,
        selectedOption = selectedTheme,
        expanded = expanded,
        onConfirm = onConfirm,
        confirmText = stringResource(id = R.string.dialog_positive_apply),
        onCancel = onCancel,
        cancelText = stringResource(id = R.string.dialog_negative_cancel)
    )
}