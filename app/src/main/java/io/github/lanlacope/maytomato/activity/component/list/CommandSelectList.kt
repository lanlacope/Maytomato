package io.github.lanlacope.maytomato.activity.component.list

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lanlacope.compose.ui.animation.FadeInAnimated
import io.github.lanlacope.compose.ui.button.combined.CombinedColumnButton
import io.github.lanlacope.compose.ui.lazy.animatedItems
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.MAYTOMATO_COPIPE
import io.github.lanlacope.maytomato.activity.component.DisplayPadding
import io.github.lanlacope.maytomato.clazz.CopipeData
import io.github.lanlacope.maytomato.clazz.rememberCopipeManager


@Composable
fun CommandSelectorList() {

    val copipeManager = rememberCopipeManager()

    val commands = remember { emptyList<CopipeData>().toMutableStateList() }

    LaunchedEffect(Unit) {
        commands.addAll(copipeManager.getCommandList())
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            animatedItems(
                items = commands,
                key = { it.title },
            ) { command ->

                CommandSelectorItem(copipeData = command)
            }
        }
    }
}

@Composable
private fun CommandSelectorItem(copipeData: CopipeData) {

    val context = LocalContext.current
    val activity = context as Activity

    val title = copipeData.title
    val text = copipeData.text

    CombinedColumnButton(
        onClick = {
            val intent = Intent().apply {
                putExtra(MAYTOMATO_COPIPE, text)
            }
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        },
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
    }
}