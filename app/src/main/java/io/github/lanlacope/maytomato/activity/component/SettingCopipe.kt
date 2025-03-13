package io.github.lanlacope.maytomato.activity.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.lanlacope.rewheel.ui.lazy.pager.LazyHorizontalPager
import io.github.lanlacope.rewheel.ui.lazy.pager.helper.PagerHelper
import io.github.lanlacope.rewheel.ui.lazy.pager.pages
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.list.AAList
import io.github.lanlacope.maytomato.activity.component.list.CommandList
import io.github.lanlacope.maytomato.activity.component.list.CopipeList


enum class CopipePage {
    COPIPE,AA,COMMAND;

    fun getString(context: Context): String {
        return when (this) {
            COPIPE -> context.getString(R.string.setting_copipe_copipe)
            AA -> context.getString(R.string.setting_copipe_aa)
            COMMAND -> context.getString(R.string.setting_copipe_command)
        }
    }
}

@Composable
fun SettingCopipe() {

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
                    CopipePage.COPIPE -> CopipeList()
                    CopipePage.AA -> AAList()
                    CopipePage.COMMAND -> CommandList()
                }
            }
        }
    }
}