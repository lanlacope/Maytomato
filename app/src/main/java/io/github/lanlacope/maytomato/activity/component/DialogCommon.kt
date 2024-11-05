package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DialogTitle(text: String) {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 10.dp)

    )
}

@Composable
fun DialogTextField(
    text: MutableState<String>,
    hint: String,
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        placeholder = {
            Text(
                text = hint,
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
            .wrapContentHeight()
            .padding(all = 8.dp)

    )
}