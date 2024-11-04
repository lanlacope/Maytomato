package io.github.lanlacope.maytomato.activity

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.lanlacope.maytomato.ui.theme.Clear
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawable(ColorDrawable(0x00000000))
        RESULT_OK
        setContent {
            MaytomatoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Clear
                ) {
                    MainDialog()
                }
            }
        }
    }
}

@Composable
private fun MainDialog() {

    val context = LocalContext.current
    val activity = context as Activity

    Dialog(
        onDismissRequest = {
            activity.finish()
        },
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Button(
                onClick = {
                    val intent = Intent().apply {
                        putExtra("replace_key", "マリー")
                    }
                    activity.setResult(RESULT_OK, intent)
                    activity.finish()
                }
            ) {
                Text(text = "マリーする！")
            }
        }
    }
}
