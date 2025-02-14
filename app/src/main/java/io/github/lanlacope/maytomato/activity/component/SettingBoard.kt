package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.maytomato.activity.component.list.BoardList
import io.github.lanlacope.maytomato.clazz.rememberBoardManager
import kotlinx.coroutines.runBlocking

@Composable
fun SettingBoard() {
    Column(modifier = Modifier.fillMaxSize()) {
        BoardList()
    }
}