package io.github.lanlacope.maytomato.activity.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lanlacope.compose.ui.lazy.pager.LazyHorizontalPager
import io.github.lanlacope.compose.ui.lazy.pager.pages
import io.github.lanlacope.maytomato.activity.component.list.AAList
import io.github.lanlacope.maytomato.activity.component.list.CommandList
import io.github.lanlacope.maytomato.activity.component.list.CopipeList


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
                    CopipePage.AA -> AAList()
                    CopipePage.COMMAND -> CommandList()
                }
            }
        }
    }
}