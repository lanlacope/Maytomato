package io.github.lanlacope.maytomato.activity.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.animation.DrawUpAnimated
import io.github.lanlacope.compose.ui.animation.FadeInAnimated
import io.github.lanlacope.compose.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.compose.ui.lazy.animatedItems
import io.github.lanlacope.compose.ui.text.search.OutlinedSearchTextField
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.activity.component.dialog.CommandAddDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CommandEditDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CommandRemoveDialog
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager
import kotlinx.coroutines.launch


@Composable
fun CommandList() {

    val scope = rememberCoroutineScope()
    val copipeManager = rememberCopipeManager()

    val listState = rememberLazyListState()

    val commands = remember { emptyList<CopipeData>().toMutableStateList() }

    LaunchedEffect(Unit) {
        commands.addAll(copipeManager.getCommandList())
    }

    var addDialogShown by rememberSaveable { mutableStateOf(false) }
    var addDialogError by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            var searchText by remember { mutableStateOf("") }

            OutlinedSearchTextField(
                text = searchText,
                onTextChange = {
                    searchText = it
                    scope.launch {
                        val searchList =
                            copipeManager.getCommandList().toMutableList().filter { command ->
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                animatedItems(
                    items = commands,
                    key = { it.text },
                ) { command ->
                    CommandItem(copipeData = command)
                }
            }
        }

        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondary,
            onClick = {
                addDialogShown = true
                addDialogError = false
            },
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    end = 30.dp,
                    bottom = 30.dp
                )
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = Icons.Default.Add.name,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(all = 8.dp)

            )
        }
    }

    CommandAddDialog(
        expanded = addDialogShown,
        onConfirm = { title, text ->
            scope.launch {
                val result = copipeManager.addCommand(title = title, text = text)
                if (result.isSuccess) {
                    commands.add(result.getOrNull()!!)
                    listState.animateScrollToItem(commands.size)
                    addDialogShown = false
                }
                else {
                    addDialogError = true
                }
            }
        },
        onCancel = { addDialogShown = false },
        isError = addDialogError
    )
}

@Composable
private fun CommandItem(copipeData: CopipeData) {

    var isRemoved by remember { mutableStateOf(false) }

    DrawUpAnimated(visible = !isRemoved) {

        val scope = rememberCoroutineScope()
        val copipeManager = rememberCopipeManager()

        var title by remember { mutableStateOf(copipeData.title) }
        var text by remember { mutableStateOf(copipeData.text) }

        var editDialogShown by rememberSaveable { mutableStateOf(false) }
        var removeDialogShown by rememberSaveable { mutableStateOf(false) }
        var editDialogError by rememberSaveable { mutableStateOf(false) }


        CombinedColumnButton(
            onClick = { editDialogShown = true },
            onLongClick = { removeDialogShown = true },
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
                    modifier = Modifier.padding(
                        start = DisplayPadding.START,
                        end = DisplayPadding.END,
                        bottom = 8.dp
                    )
                )
            }

            CommandEditDialog(
                expanded = editDialogShown,
                title = title,
                text = text,
                onConfirm = { newTitle, newText ->
                    scope.launch {
                        val result = copipeManager.editCommand(
                            title = newTitle,
                            text = newText,
                            lastText = text
                        )
                        if (result.isSuccess) {
                            title = newTitle
                            text = newText
                            editDialogShown = false
                        }
                        else {
                            editDialogError = true
                        }
                    }
                },
                onCancel = { editDialogShown = false },
                isError = editDialogError
            )

            CommandRemoveDialog(
                expanded = removeDialogShown,
                title = title,
                onConfirm = { removedTitle ->
                    scope.launch {
                        copipeManager.removeCommand(text)
                        isRemoved = true
                        removeDialogShown = false
                    }
                },
                onCancel = { removeDialogShown = false }
            )
        }
    }
}