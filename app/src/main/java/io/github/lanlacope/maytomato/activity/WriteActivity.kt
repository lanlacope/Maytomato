package io.github.lanlacope.maytomato.activity

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.component.WriteDialog
import io.github.lanlacope.maytomato.clazz.BoardManager
import io.github.lanlacope.maytomato.ui.theme.MaytomatoTheme
import kotlinx.coroutines.runBlocking


class WriteActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originIntent = this.intent

        println(
            "Action:    ${originIntent.action}\n" +
            "Categoty:  ${originIntent.categories}\n" +
            "Data:      ${originIntent.data}\n"
        )

        val extras = originIntent.extras ?: Bundle()

        val intentKeys = extras.keySet()

        println("--- Extras ---")
        for (key in intentKeys) {
            val value = extras.get(key)
            println("Key: $key, Value: $value\n")
        }

        val bbsInfo = try {
            BbsInfo.parse(originIntent.dataString!!)
        } catch (e: Exception) {
            finish()
            return
        }

        val bbsSetting = runBlocking {
            BoardManager(this@WriteActivity).getBoardList().firstOrNull { boardSetting ->
                bbsInfo.domain.endsWith(boardSetting.domain) && boardSetting.enabled
            }
        }

        println(bbsSetting)

        if (bbsSetting != null) {
            val url = originIntent.dataString ?: ""

            if (url.isThread() || url.isBoard()) {
                setContent {
                    MaytomatoTheme {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = Color.Transparent
                        ) {
                            WriteDialog(bbsInfo, bbsSetting)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    this.getString(R.string.toast_error_unread_url),
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return
            }
        }
        else {
            originIntent.component = ComponentName(
                ChmateWriteComponent.APP_NAME,
                ChmateWriteComponent.ACTIVITY_NAME
            )

            try {
                startActivity(originIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    this.getString(R.string.toast_error_nonexist_chmate),
                    Toast.LENGTH_LONG
                ).show()
            }
            finish()
            return
        }
    }
}

private fun String.isThread() = this.matches(Regex("""^https?://[^/]+/test/read.cgi/[^/]+/\d+/$"""))
private fun String.isBoard() = this.matches(Regex("""^https?://[^/]+/[^/]+/$"""))

data class BbsInfo(
    val protocol: String,
    val domain: String,
    val bbs: String,
    val key: String? = null
) {

    companion object {
        fun parse(url: String): BbsInfo {

            if (url.isThread()) {
                val parseedLinks = Regex("""^(https?://)([^/]+)/test/read\.cgi/([^/]+)/(\d+)/$""")
                    .find(url)!!.groups.mapIndexed { index, matchGroup ->
                        matchGroup!!.value
                    }

                return BbsInfo(
                    protocol = parseedLinks[1],
                    domain = parseedLinks[2],
                    bbs = parseedLinks[3],
                    key = parseedLinks[4]
                )
            } else if (url.isBoard()) {
                val parseedLinks = Regex("""^(https?://)([^/]+)/([^/]+)/$""")
                    .find(url)!!.groups.mapIndexed { index, matchGroup ->
                        matchGroup!!.value
                    }

                return BbsInfo(
                    protocol = parseedLinks[1],
                    domain = parseedLinks[2],
                    bbs = parseedLinks[3]
                )
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}


private object ChmateWriteComponent {
    const val APP_NAME = "jp.co.airfront.android.a2chMate"
    const val ACTIVITY_NAME = "jp.syoboi.a2chMate.activity.ResEditActivity\$Dialog"
}

