package io.github.lanlacope.maytomato.activity.component.list

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.animation.DrawUpAnimated
import io.github.lanlacope.compose.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.compose.ui.lazy.animatedItems
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.activity.component.dialog.BoardAddDialog
import io.github.lanlacope.maytomato.activity.component.dialog.BoardEditDialog
import io.github.lanlacope.maytomato.activity.component.dialog.BoardRemoveDialog
import io.github.lanlacope.maytomato.clazz.BoardSetting
import io.github.lanlacope.maytomato.clazz.rememberBoardManager
import io.github.lanlacope.maytomato.clazz.rememberCookieManager
import kotlinx.coroutines.launch


@Composable
fun BoardList() {

    val scope = rememberCoroutineScope()
    val boardManager = rememberBoardManager()
    val listState = rememberLazyListState()

    val boards = remember { emptyList<BoardSetting>().toMutableStateList() }

    LaunchedEffect(Unit) {
        boards.addAll(boardManager.getBoardList())
    }

    var addDialogShown by rememberSaveable { mutableStateOf(false) }
    var addDialogError by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                animatedItems(
                    items = boards,
                    key = { it.domain },
                ) { board ->
                    BoardItem(boardSetting = board)
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

    BoardAddDialog(
        expanded = addDialogShown,
        onConfirm = { newBoardSetting ->
            scope.launch {
                val result = boardManager.addBoard(newBoardSetting)
                if (result.isSuccess) {
                    boards.add(result.getOrNull()!!)
                    listState.animateScrollToItem(boards.size)
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
private fun BoardItem(boardSetting: BoardSetting) {

    var isRemoved by remember { mutableStateOf(false) }

    DrawUpAnimated(visible = !isRemoved) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val boardManager = rememberBoardManager()
        val cookieManager = rememberCookieManager()

        var iBoardSetting by remember { mutableStateOf(boardSetting.copy()) }

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
                text = iBoardSetting.domain,
                fontSize = 12.sp,
                maxLines = 1,
                modifier = Modifier.padding(
                    start = DisplayPadding.START,
                    end = DisplayPadding.END
                )
            )

            Text(
                text = if (iBoardSetting.enabled) "有効" else "無効",
                fontSize = 8.sp,
                fontFamily = FontFamily((Font(resId = R.font.mona))),
                lineHeight = 8.sp,
                modifier = Modifier
                    .padding(
                        start = DisplayPadding.START,
                        end = DisplayPadding.END,
                        bottom = 8.dp
                    )
            )

            BoardEditDialog(
                expanded = editDialogShown,
                boardSetting = iBoardSetting,
                onConfirm = { newBoardSetting ->
                    scope.launch {
                        val result = boardManager.editBoard(newBoardSetting)
                        if (result.isSuccess) {
                            iBoardSetting = result.getOrNull()!!
                            editDialogShown = false
                            Toast.makeText(context, context.getString(R.string.setting_board_edit_success), Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(context, context.getString(R.string.setting_board_edit_failed), Toast.LENGTH_LONG).show()
                        }
                    }
                },
            )

            BoardRemoveDialog(
                expanded = removeDialogShown,
                domain = iBoardSetting.domain,
                onConfirm = { removedTitle ->
                    scope.launch {
                        boardManager.removeBoard(iBoardSetting.domain)
                        cookieManager.removeCookie(iBoardSetting.domain)
                        isRemoved = true
                        removeDialogShown = false
                    }
                },
                onCancel = { removeDialogShown = false }
            )
        }
    }
}