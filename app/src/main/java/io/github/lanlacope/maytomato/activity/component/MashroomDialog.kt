package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.collection.collection.toggle
import io.github.lanlacope.compose.composeable.ui.click.BoxButton
import io.github.lanlacope.compose.ui.action.option.CompactOptionCheckBox
import io.github.lanlacope.compose.ui.busy.manu.BusyManu
import io.github.lanlacope.compose.ui.busy.option.texts
import io.github.lanlacope.compose.ui.button.layout.ManuButton
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.rememberCopipeSelectResult
import io.github.lanlacope.maytomato.clazz.ConvertMode
import io.github.lanlacope.maytomato.clazz.ConvertNumber
import io.github.lanlacope.maytomato.clazz.ConvertOption
import io.github.lanlacope.maytomato.clazz.rememberStringConverter

@Composable
fun MashroomDialog() {

    val context = LocalContext.current
    val activity = context as Activity
    val stringConverter = rememberStringConverter()

    GrowDialog(
        expanded = true,
        onDismissRequest = { activity.finish() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Spacer(modifier = Modifier.height(8.dp))

            var selectedMode by remember { mutableStateOf(ConvertMode.MOJIBAKE) }
            var modeManuShown by remember { mutableStateOf(false) }
            val modes = remember {
                mutableStateMapOf(
                    ConvertMode.ALL to context.getString(R.string.manu_text_mode_all),
                    ConvertMode.SKIP_BR to context.getString(R.string.manu_text_mode_slip_br),
                    ConvertMode.MOJIBAKE to context.getString(R.string.manu_text_mode_mojibake),
                    ConvertMode.REMOVE to context.getString(R.string.manu_text_mode_remove)
                )
            }

            var selectedNumber by remember { mutableIntStateOf(ConvertNumber.DEC) }
            var numberManuShown by remember { mutableStateOf(false) }
            val numbers = remember {
                mutableStateMapOf(
                    ConvertNumber.DEC to context.getString(R.string.manu_text_number_decimal),
                    ConvertNumber.HEX to context.getString(R.string.manu_text_number_hexadecima)
                )
            }

            Row {
                ManuButton(
                    text = modes[selectedMode]!!,
                    onClick = { modeManuShown = true }
                ) {
                    BusyManu(
                        expanded = modeManuShown,
                        onDismissRequest = { modeManuShown = false }
                    ) {
                        texts(
                            options = modes,
                            onClick = {
                                selectedMode = it
                                modeManuShown = false
                            }
                        )
                    }
                }

                ManuButton(
                    text = numbers[selectedNumber]!!,
                    onClick = { numberManuShown = true }
                ) {
                    BusyManu(
                        expanded = numberManuShown,
                        onDismissRequest = { numberManuShown = false }
                    ) {
                        texts(
                            options = numbers,
                            onClick = {
                                selectedNumber = it
                                numberManuShown = false
                            }
                        )
                    }
                }
            }

            val selectedOptions = remember { mutableStateListOf<String>() }

            CompactOptionCheckBox(
                text = stringResource(id = R.string.manu_text_option_entity),
                checked = selectedOptions.contains(ConvertOption.ENTITY),
                onClick = {
                    selectedOptions.toggle(ConvertOption.ENTITY)
                },
                modifier = Modifier.fillMaxWidth()
            )

            var text by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.dialog_hint_message),
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            color = Gray
                        ),
                        modifier = Modifier.wrapContentSize()
                    )
                },
                minLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = false)
                    .padding(all = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(50.dp)
            ) {
                val copipeSelectResult = rememberCopipeSelectResult { copipe ->
                    // いい感じに改行
                    text = if (text.isEmpty() || text.last() == '\n') "$text$copipe" else "$text\n$copipe"
                }

                BoxButton(
                    contentAlignment = Alignment.Center,
                    onClick = {
                        copipeSelectResult.launch()
                    },
                    innerPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.setting_copipe),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.weight(1.0f))

                BoxButton(
                    onClick = {
                        val output =
                            stringConverter.startConvert(
                                rawText = text,
                                mode = selectedMode,
                                number = selectedNumber,
                                options = selectedOptions
                            )
                        val intent = Intent().apply {
                            putExtra(Simeji.REPLACE_KEY, output)
                        }
                        activity.setResult(Activity.RESULT_OK, intent)
                        activity.finish()
                    },
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_positive_convert),
                        fontSize = 24.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private object Simeji {
    const val REPLACE_KEY = "replace_key"
}