package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.clazz.ConvertMode
import io.github.lanlacope.maytomato.clazz.ConvertNumber
import io.github.lanlacope.maytomato.clazz.rememberStringConverter
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ConvertDialog() {

    val context = LocalContext.current
    val activity = context as Activity
    val stringConverter = rememberStringConverter()

    Dialog(
        onDismissRequest = {
           activity.finish()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    MaterialTheme.colorScheme.background
                )

        ) {

            val text = remember { mutableStateOf("") }
            var selectedMode by remember { mutableStateOf(ConvertMode.SELECTOR_HIGH) }
            var selectedNumber by remember { mutableStateOf(ConvertNumber.DEC.toString()) }

            DialogTitle(text = stringResource(id = R.string.dialog_title_convert))

            val modes = persistentListOf(
                ConvertMode.ALL,
                ConvertMode.SKIP_BR,
                ConvertMode.SELECTOR_HIGH,
                ConvertMode.SELECTOR_LOW,
                ConvertMode.REMOVE,
            )
            val modeText: (String) -> String = { selectedText ->
                when (selectedText) {
                    ConvertMode.ALL -> context.getString(R.string.manu_text_mode_all)
                    ConvertMode.SKIP_BR -> context.getString(R.string.manu_text_mode_slip_br)
                    ConvertMode.SELECTOR_HIGH -> context.getString(R.string.manu_text_mode_selector_high)
                    ConvertMode.SELECTOR_LOW -> context.getString(R.string.manu_text_mode_selector_low)
                    ConvertMode.REMOVE -> context.getString(R.string.manu_text_mode_remove)
                    else -> throw Exception()
                }
            }
            val refrectionModeManu: (String) -> Unit = { newMode ->
                selectedMode = newMode
            }
            OptionManu(
                selectedText = selectedMode,
                options = modes,
                optionText = modeText,
                refrection = refrectionModeManu
            )

            val numbers = persistentListOf(
                ConvertNumber.DEC.toString(),
                ConvertNumber.HEX.toString()
            )
            val numberText: (String) -> String = { selectedText ->
                when (selectedText) {
                    ConvertNumber.DEC.toString() -> context.getString(R.string.manu_text_number_decimal)
                    ConvertNumber.HEX.toString() -> context.getString(R.string.manu_text_number_hexadecima)
                    else -> throw Exception()
                }
            }
            val refrectionNumberManu: (String) -> Unit = { newNumber ->
                selectedNumber = newNumber
            }
            OptionManu(
                selectedText = selectedNumber,
                options = numbers,
                optionText = numberText,
                refrection = refrectionNumberManu
            )

            DialogTextField(
                text = text,
                hint = stringResource(id = R.string.dialog_hint_convert)
            )

            TextButton(
                onClick = {
                    val output =
                        stringConverter.convert(text.value, selectedMode, selectedNumber.toInt())
                    val intent = Intent().apply {
                        putExtra("replace_key", output)
                    }
                    activity.setResult(Activity.RESULT_OK, intent)
                    activity.finish()
                },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(text = stringResource(id = R.string.dialog_positive_convert))
            }
        }
    }
}