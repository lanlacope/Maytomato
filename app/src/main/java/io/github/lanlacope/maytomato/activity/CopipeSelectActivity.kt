package io.github.lanlacope.maytomato.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.github.lanlacope.maytomato.activity.component.CopipeSelector
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme

class CopipeSelectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaytomatoTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CopipeSelector()
                }
            }
        }
    }

    class Contract : ActivityResultContract<Unit, Result<String>>() {

        override fun createIntent(context: Context, input: Unit): Intent {
            val intentSelect = Intent(context, CopipeSelectActivity::class.java)
            intentSelect.setAction(ContactsContract.Intents.ACTION_VOICE_SEND_MESSAGE_TO_CONTACTS)
            intentSelect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intentSelect
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Result<String> {
            if (resultCode == RESULT_OK) {
                return Result.success(intent?.action ?: "")
            } else {
                return Result.failure(Exception())
            }
        }
    }
}

const val MAYTOMATO_COPIPE = "maytomato_copipe"

@Stable
data class CopipeSelectResultLauncher(
    private val launcher: ManagedActivityResultLauncher<Unit, Result<String>>,
) {
    fun launch() {
        launcher.launch(Unit)
    }
}

@Composable
fun rememberCopipeSelectResult(
    onSelected: (String) -> Unit,
): CopipeSelectResultLauncher {
    val launcher = rememberLauncherForActivityResult(
        contract = CopipeSelectActivity.Contract()
    ) { result ->
        if (result.isSuccess) {
            onSelected(result.getOrNull()!!)
        }
    }
    return remember {
        CopipeSelectResultLauncher(launcher = launcher)
    }
}