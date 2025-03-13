package io.github.lanlacope.maytomato.activity.component.list

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.rewheel.ui.animation.FadeInAnimated
import io.github.lanlacope.rewheel.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.rewheel.ui.lazy.animatedItems
import io.github.lanlacope.rewheel.ui.text.search.SearchTextField
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.MAYTOMATO_COPIPE
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager
import kotlinx.coroutines.launch


@Composable
fun CommandSelectorList() {

    val scope = rememberCoroutineScope()
    val copipeManager = rememberCopipeManager()

    val commands = remember { emptyList<CopipeData>().toMutableStateList() }

    LaunchedEffect(Unit) {
        commands.addAll(copipeManager.getCommandList())
    }

    Column(modifier = Modifier.fillMaxSize()) {

        var searchText by remember { mutableStateOf("") }

        SearchTextField(
            text = searchText,
            onTextChange = {
                searchText = it
                scope.launch {
                    val searchList = copipeManager.getCommandList().toMutableList().filter { command ->
                        if (it.isNotEmpty()) {
                            command.title.contains(it) || command.text.contains(it)
                        } else {
                            true
                        }
                    }
                    commands.clear()
                    commands.addAll(searchList)
                }
            },
            hintText = stringResource(id = R.string.hint_command_search),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            animatedItems(
                items = commands,
                key = { it.text },
            ) { command ->
                CommandSelectorItem(copipeData = command)
            }
        }
    }
}

@Composable
private fun CommandSelectorItem(copipeData: CopipeData) {

    val context = LocalContext.current
    val activity = context as Activity

    val title = copipeData.title
    val text = copipeData.text

    CombinedColumnButton(
        onClick = {
            val intent = Intent().apply {
                putExtra(MAYTOMATO_COPIPE, text)
            }
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        },
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp),
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            maxLines = 1,
            modifier = Modifier.padding(
                start = DisplayPadding.START,
                end = DisplayPadding.END
            )
        )

        FadeInAnimated(visible = text.isNotEmpty()) {

            Text(
                text = text,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .padding(
                        start = DisplayPadding.START,
                        end = DisplayPadding.END,
                        bottom = 8.dp
                    )
            )
        }
    }
}