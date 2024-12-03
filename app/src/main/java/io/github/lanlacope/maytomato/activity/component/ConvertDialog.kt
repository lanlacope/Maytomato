package io.github.lanlacope.maytomato.activity.component

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.github.lanlacope.collection.collection.toggle
import io.github.lanlacope.compose.ui.action.option.CompactOptionCheckBox
import io.github.lanlacope.compose.ui.busy.manu.BusyManu
import io.github.lanlacope.compose.ui.busy.option.text
import io.github.lanlacope.compose.ui.button.layout.ManuButton
import io.github.lanlacope.compose.ui.dialog.GrowDialog
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.clazz.ConvertMode
import io.github.lanlacope.maytomato.clazz.ConvertNumber
import io.github.lanlacope.maytomato.clazz.ConvertOption
import io.github.lanlacope.maytomato.clazz.propaty.Simeji
import io.github.lanlacope.maytomato.clazz.rememberStringConverter

@Composable
fun ConvertDialog() {

    val context = LocalContext.current
    val activity = context as Activity
    val stringConverter = rememberStringConverter()

    GrowDialog(
        expanded = true,
        onDismissRequest = { activity.finish() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {

            val (titleRef, contentRef, buttonRef) = createRefs()

            var text by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                .constrainAs(contentRef) {
                    top.linkTo(titleRef.bottom)
                    bottom.linkTo(buttonRef.top)
                    width = Dimension.matchParent
                }
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.dialog_hint_convert),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                color = Gray
                            ),
                            modifier = Modifier.wrapContentSize()
                        )
                    },
                    minLines = 3,
                    modifier = Modifier.padding(all = 8.dp)
                )
            }

            var selectedMode by remember { mutableStateOf(ConvertMode.MOJIBAKE) }
            var selectedNumber by remember { mutableIntStateOf(ConvertNumber.DEC) }
            val selectedOptions = remember { mutableStateListOf<String>() }

            Column(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        width = Dimension.matchParent
                    }
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = stringResource(id = R.string.dialog_title_convert),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(all = 10.dp)

                )

                var modeManuShown by remember { mutableStateOf(false) }
                val modes = remember {
                    mutableStateMapOf(
                        ConvertMode.ALL to context.getString(R.string.manu_text_mode_all),
                        ConvertMode.SKIP_BR to context.getString(R.string.manu_text_mode_slip_br),
                        ConvertMode.MOJIBAKE to context.getString(R.string.manu_text_mode_mojibake),
                        ConvertMode.REMOVE to context.getString(R.string.manu_text_mode_remove)
                    )
                }

                ManuButton(
                    text = modes[selectedMode]!!,
                    onClick = { modeManuShown = true }
                ) {
                    BusyManu(
                        expanded = modeManuShown,
                        onDismissRequest = { modeManuShown = false }
                    ) {
                        text(
                            options = modes,
                            onClick = {
                                selectedMode = it
                                modeManuShown = false
                            }
                        )
                    }
                }

                var numberManuShown by remember { mutableStateOf(false) }
                val numbers = remember {
                    mutableStateMapOf(
                        ConvertNumber.DEC to context.getString(R.string.manu_text_number_decimal),
                        ConvertNumber.HEX to context.getString(R.string.manu_text_number_hexadecima)
                    )
                }

                ManuButton(
                    text = numbers[selectedNumber]!!,
                    onClick = { numberManuShown = true }
                ) {
                    BusyManu(
                        expanded = numberManuShown,
                        onDismissRequest = { numberManuShown = false }
                    ) {
                        text(
                            options = numbers,
                            onClick = {
                                selectedNumber = it
                                numberManuShown = false
                            }
                        )
                    }
                }

                CompactOptionCheckBox(
                    text = stringResource(id = R.string.manu_text_option_entity),
                    checked = selectedOptions.contains(ConvertOption.ENTITY),
                    onClick = {
                        selectedOptions.toggle(ConvertOption.ENTITY)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .constrainAs(buttonRef) {
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                    }
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
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
                    }
                ) {
                    Text(text = stringResource(id = R.string.dialog_positive_convert))
                }
            }
        }
    }
}