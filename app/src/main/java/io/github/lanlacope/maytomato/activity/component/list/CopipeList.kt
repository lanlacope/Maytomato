package io.github.lanlacope.maytomato.activity.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.animation.DrawUpAnimated
import io.github.lanlacope.compose.ui.animation.FadeInAnimated
import io.github.lanlacope.compose.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.compose.ui.lazy.animatedItems
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeAddDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeEditDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeRemoveDialog
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager
import kotlinx.coroutines.launch


@Composable
fun CopipeList() {

    val scope = rememberCoroutineScope()
    val copipeManager = rememberCopipeManager()

    val listState = rememberLazyListState()

    val copipes = remember { emptyList<CopipeData>().toMutableStateList() }

    LaunchedEffect(Unit) {
        copipes.addAll(copipeManager.getCopipeList())
    }

    var addDialogShown by rememberSaveable { mutableStateOf(false) }
    var addDialogError by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            animatedItems(
                items = copipes,
                key = { it.title },
            ) { copipe ->

                CopipeItem(copipeData = copipe)
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

    CopipeAddDialog(
        expanded = addDialogShown,
        onConfirm = { title, text ->
            scope.launch {
                val result = copipeManager.addCopipe(title = title, text = text)
                if (result.isSuccess) {
                    copipes.add(result.getOrNull()!!)
                    listState.animateScrollToItem(copipes.size)
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
private fun CopipeItem(copipeData: CopipeData) {

    var isRemoved by remember { mutableStateOf(false) }

    DrawUpAnimated(visible = !isRemoved) {

        val scope = rememberCoroutineScope()
        val copipeManager = rememberCopipeManager()

        var title by remember { mutableStateOf(copipeData.title) }
        var text by remember { mutableStateOf(copipeData.text) }

        var editDialogShown by rememberSaveable { mutableStateOf(false) }
        var removeDialogShown by rememberSaveable { mutableStateOf(false) }

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
                    fontSize = 8.sp,
                    maxLines = 3,
                    modifier = Modifier.padding(
                        start = DisplayPadding.START,
                        end = DisplayPadding.END,
                        bottom = 8.dp
                    )
                )
            }

            CopipeEditDialog(
                expanded = editDialogShown,
                title = title,
                text = text,
                onConfirm = { newTitle, newText ->
                    scope.launch {
                        copipeManager.editCopipe(
                            title = newTitle,
                            text = newText,
                            lastTitle = title
                        )
                        title = newTitle
                        text = newText
                        editDialogShown = false
                    }
                },
                onCancel = { editDialogShown = false }
            )

            CopipeRemoveDialog(
                expanded = removeDialogShown,
                title = title,
                onConfirm = { removedTitle ->
                    scope.launch {
                        copipeManager.removeCopipe(title)
                        isRemoved = true
                        removeDialogShown = false
                    }
                },
                onCancel = { removeDialogShown = false }
            )
        }
    }
}