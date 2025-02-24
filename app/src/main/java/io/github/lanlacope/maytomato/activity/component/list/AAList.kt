package io.github.lanlacope.maytomato.activity.component.list

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.animation.DrawUpAnimated
import io.github.lanlacope.compose.ui.animation.FadeInAnimated
import io.github.lanlacope.compose.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.compose.ui.lazy.animatedItems
import io.github.lanlacope.compose.ui.text.search.OutlinedSearchTextField
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.activity.component.dialog.AAAddDialog
import io.github.lanlacope.maytomato.activity.component.dialog.AAEditDialog
import io.github.lanlacope.maytomato.activity.component.dialog.AARemoveDialog
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager
import kotlinx.coroutines.launch


@Composable
fun AAList() {

    val scope = rememberCoroutineScope()
    val copipeManager = rememberCopipeManager()

    val listState = rememberLazyListState()

    val aas = remember { emptyList<CopipeData>().toMutableStateList() }

    LaunchedEffect(Unit) {
        aas.addAll(copipeManager.getAaList())
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
                            copipeManager.getAaList().toMutableList().filter { aa ->
                                if (it.isNotEmpty()) {
                                    aa.title.contains(it) || aa.text.contains(it)
                                } else {
                                    true
                                }
                            }
                        aas.clear()
                        aas.addAll(searchList)
                    }
                },
                hintText = stringResource(id = R.string.hint_aa_search),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                animatedItems(
                    items = aas,
                    key = { it.text },
                ) { aa ->
                    AAItem(copipeData = aa)
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

    AAAddDialog(
        expanded = addDialogShown,
        onConfirm = { title, text ->
            scope.launch {
                val result = copipeManager.addAa(title = title, text = text)
                if (result.isSuccess) {
                    aas.add(result.getOrNull()!!)
                    listState.animateScrollToItem(aas.size)
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
private fun AAItem(copipeData: CopipeData) {

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
                    fontSize = 8.sp,
                    fontFamily = FontFamily((Font(resId = R.font.mona))),
                    lineHeight = 8.sp,
                    modifier = Modifier
                        .padding(
                            start = DisplayPadding.START,
                            end = DisplayPadding.END,
                            bottom = 8.dp
                        )
                        .horizontalScroll(rememberScrollState())
                )
            }

            AAEditDialog(
                expanded = editDialogShown,
                title = title,
                text = text,
                onConfirm = { newTitle, newText ->
                    scope.launch {
                        val result = copipeManager.editAa(
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

            AARemoveDialog(
                expanded = removeDialogShown,
                title = title,
                onConfirm = { removedTitle ->
                    scope.launch {
                        copipeManager.removeAa(text)
                        isRemoved = true
                        removeDialogShown = false
                    }
                },
                onCancel = { removeDialogShown = false }
            )
        }
    }
}