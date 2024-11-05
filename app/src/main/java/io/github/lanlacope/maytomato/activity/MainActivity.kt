package io.github.lanlacope.maytomato.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.lanlacope.maytomato.activity.component.ConvertDialog
import io.github.lanlacope.maytomato.ui.theme.Clear
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaytomatoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Clear
                ) {
                    ConvertDialog()
                }
            }
        }
    }
}



