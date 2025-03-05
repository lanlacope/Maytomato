package io.github.lanlacope.maytomato.activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lanlacope.maytomato.activity.component.SettingAbout
import io.github.lanlacope.maytomato.activity.component.SettingBoard
import io.github.lanlacope.maytomato.activity.component.SettingCopipe
import io.github.lanlacope.maytomato.activity.component.SettingRoot
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val path = SettingNavi.fromString(intent.dataString?.replace("maytomato:", "") ?: "")

        setContent {
            MaytomatoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingView(path)
                }
            }
        }
    }
}

enum class SettingNavi {
    ROOT, COPIPE, BOARD, ABOUT;

    override fun toString(): String {
        return when (this) {
            ROOT -> "Root"
            COPIPE -> "Copipe"
            BOARD -> "Board"
            ABOUT -> "About"
        }
    }

    companion object {
        fun fromString(value: String): SettingNavi {
            return SettingNavi.entries.find { it.toString() == value } ?: ROOT
        }
    }
}

val SETTING_MINHEIGHT = 80.dp

@Composable
fun SettingView(path: SettingNavi = SettingNavi.ROOT) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = path.toString()
    ) {
        composable(SettingNavi.ROOT.toString()) { SettingRoot(navController) }
        composable(SettingNavi.COPIPE.toString()) { SettingCopipe() }
        composable(SettingNavi.BOARD.toString()) { SettingBoard() }
        composable(SettingNavi.ABOUT.toString()) { SettingAbout() }
    }
}