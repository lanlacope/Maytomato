package io.github.lanlacope.maytomato.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.lanlacope.maytomato.activity.component.MushroomDialog
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme

class MushroomActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeFoundationFlags.isNewContextMenuEnabled = false
        setContent {
            MaytomatoTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Transparent
                ) {
                    MushroomDialog()
                }
            }
        }
    }
}



