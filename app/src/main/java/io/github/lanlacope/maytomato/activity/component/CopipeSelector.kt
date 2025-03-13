package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.lanlacope.maytomato.activity.component.list.AASelectorList
import io.github.lanlacope.maytomato.activity.component.list.CommandSelectorList
import io.github.lanlacope.maytomato.activity.component.list.CopipeSelectorList
import io.github.lanlacope.rewheel.ui.lazy.pager.LazyHorizontalPager
import io.github.lanlacope.rewheel.ui.lazy.pager.helper.PagerHelper
import io.github.lanlacope.rewheel.ui.lazy.pager.pages

@Composable
fun CopipeSelector() {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {

        val listState = rememberLazyListState()

        PagerHelper(
            items = CopipePage.entries.toList().map {
                it.getString(context)
            },
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        LazyHorizontalPager(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            pages(
                items = CopipePage.entries.toList(),
            ) { page ->
                when(page) {
                    CopipePage.COPIPE -> CopipeSelectorList()
                    CopipePage.AA -> AASelectorList()
                    CopipePage.COMMAND -> CommandSelectorList()
                }
            }
        }
    }
}