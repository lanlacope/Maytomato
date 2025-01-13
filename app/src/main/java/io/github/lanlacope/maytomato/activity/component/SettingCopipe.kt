package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import io.github.lanlacope.compose.ui.lazy.pager.LazyHorizontalPager
import io.github.lanlacope.compose.ui.lazy.pager.pages
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeAddDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeEditDialog
import io.github.lanlacope.maytomato.activity.component.dialog.CopipeRemoveDialog
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager
import kotlinx.coroutines.launch


enum class CopipePage {
    COPIPE,AA,COMMAND;
}

@Composable
fun SettingCopipe() {

    Column(modifier = Modifier.fillMaxSize()) {

        LazyHorizontalPager(modifier = Modifier.fillMaxSize()) {

            pages(
                items = CopipePage.entries.toList(),
            ) { page ->
                when(page) {
                    CopipePage.COPIPE -> CopipeList()
                    CopipePage.AA -> CopipeList()
                    CopipePage.COMMAND -> CopipeList()
                }
            }
        }
    }
}