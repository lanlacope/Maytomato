package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.lanlacope.maytomato.widgit.Box
import kotlinx.collections.immutable.PersistentList

@Composable
fun OptionManu(
    selectedText: String,
    options: PersistentList<String>,
    optionText: (String) -> String,
    refrection: (String) -> Unit
) {

    var shown by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        onClick = {
            shown = true
        },
        modifier = Modifier
            .padding(all = 10.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))

    ) {
        Row {
            Text(
                text = optionText(selectedText),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "contentDescription",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        DropdownMenu(
            expanded = shown,
            onDismissRequest = {
                shown = false
            }

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text (text = optionText(option)) },
                    onClick = {
                        refrection(option)
                        shown = false
                    }
                )
            }
        }
    }
}