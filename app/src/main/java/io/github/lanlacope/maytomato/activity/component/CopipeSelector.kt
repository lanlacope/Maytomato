package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lanlacope.compose.ui.lazy.pager.LazyHorizontalPager
import io.github.lanlacope.compose.ui.lazy.pager.pages
import io.github.lanlacope.maytomato.activity.component.list.AASelectorList
import io.github.lanlacope.maytomato.activity.component.list.CommandSelectorList
import io.github.lanlacope.maytomato.activity.component.list.CopipeSelectorList

@Composable
fun CopipeSelector() {

    Column(modifier = Modifier.fillMaxSize()) {

        LazyHorizontalPager(modifier = Modifier.fillMaxSize()) {

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