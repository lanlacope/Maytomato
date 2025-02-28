package io.github.lanlacope.maytomato.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
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

    @SuppressLint("UnsafeIntentLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originIntent = this.intent
        val url = originIntent.dataString ?: ""

        println(originIntent)
        println("--- Data ---")
        println(originIntent.data)
        println("--- Extras ---")
        val extras = originIntent.extras ?: Bundle()
        val intentKeys = extras.keySet()
        for (key in intentKeys) {
            val value = extras.get(key)
            println("Key: $key, Value: $value\n")
        }



        val bbsInfo = try {
            BbsInfo.parse(url)
        } catch (e: Exception) {
            // 仮対応
            BbsInfo(
                domain = "",
                protocol = "",
                bbs = ""
            )
        }

        val boardSetting = runBlocking {
            BoardManager(this@WriteActivity).getBoardList().firstOrNull { boardSetting ->
                bbsInfo.domain.endsWith(boardSetting.domain) && boardSetting.enabled
            }
        }

        if (boardSetting != null) {

            if (url.isThread() || url.isBoard()) {
                val (title, res) = originIntent.parseDefaultMessage()
                setContent {
                    MaytomatoTheme {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = Color.Transparent
                        ) {
                            WriteDialog(
                                defaultSubject = title,
                                defaultMessage = res,
                                bbsInfo = bbsInfo,
                                boardSetting = boardSetting
                            )
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
            originIntent.data = Uri.parse(originIntent.dataString?.replace("shitaraba.net", "livedoor.jp"))
            originIntent.component = ComponentName(
                ChmateString.APP_NAME,
                ChmateString.ACTIVITY_NAME
            )

            try {
                @Suppress("UnsafeIntentLaunch")
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

private fun Intent.parseDefaultMessage(): Pair<String, String> {
    val originalUrl = this@parseDefaultMessage.getStringExtra(ChmateString.EXTRAS_URL) ?: ""
    val originalTitle = this@parseDefaultMessage.getStringExtra(ChmateString.EXTRAS_TITLE) ?: ""
    val originalRes = this@parseDefaultMessage.getStringExtra(ChmateString.EXTRAS_1RES) ?: ""

    val (title, res) = createNewThreadMessage(url = originalUrl, title = originalTitle, res = originalRes)

    return Pair(
        buildString { // タイトル
            if (title.isNotEmpty()) append(title)
        },
        buildString { // 本文
            val anchor = this@parseDefaultMessage.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            if (anchor.isNotEmpty()) appendLine(anchor)
            if (res.isNotEmpty()) append(res)
        }
    )
}

private fun createNewThreadMessage(url: String, title: String, res: String): Pair<String, String> {
    try {
        // 括弧の中の数字は含めない
        val titleMatchResult =
            Regex("""^(?:\s*([\[［〈《【][^\[［〈《【\]］〉》】]*[]］〉》】])\s*)*(.*?)(?:\s*([\[［〈《【][^\[［〈《【\]］〉》】]*[]］〉》】])\s*)*$""").find(
                title
            )
        val (prefix, main, suffix) = titleMatchResult!!.destructured
        val numberMatchResult = Regex("""^(.*?)(\d+)(?!.*\d)(.*)$""").find(main)
        val newTitle = if (numberMatchResult != null) {
            val (prefixMain, number, suffixMain) = numberMatchResult.destructured
            "${prefix}${prefixMain}${number.toInt() + 1}${suffixMain}${suffix}"
        } else if (title.isNotEmpty()) {
            "${prefix}${main} ★1${suffix}"
        } else {
            ""
        }

        // <hr>(-)以降は消す
        val cutRes = res.replace(Regex("""\n^-$[\s\S]*""", RegexOption.MULTILINE), "")
        val resMatchResult = Regex("""([\s\S]*?)(^.*前スレ.*$)([\s\S]*)""",  RegexOption.MULTILINE).find(cutRes)

        val newRes = if (resMatchResult != null) {
            println("matched")
            val (prefixRes, preThread, suffixRes) = resMatchResult.destructured
            "${prefixRes}${preThread}\n${title}\n${url}${suffixRes}"
        } else if (res.isNotEmpty()) {
            println("unmatched")
            if (cutRes.isNotEmpty()) "$cutRes\n\n※前スレ\n${title}\n${url}"
            else "※前スレ\n${title}\n${url}"
        } else {
            ""
        }

        return Pair(newTitle, newRes)
    }
    catch (e: Exception) {
        return Pair("", "")
    }
}

data class BbsInfo(
    val protocol: String,
    val domain: String,
    val bbs: String,
    val key: String? = null
) {

    fun toUri(): Uri {
        return if (key.isNullOrEmpty()) {
            Uri.parse("${protocol}//${domain}/${bbs}/")
        } else {
            Uri.parse("${protocol}//${domain}/test/read.cgi/${bbs}/${key}/")
        }
    }

    companion object {
        fun parse(url: String): BbsInfo {

            if (url.isThread()) {
                val matchResult = Regex("""^(https?://)([^/]+)/test/read\.cgi/([^/]+)/(\d+)/$""").find(url)
                val (protocol, domain, bbs, key) = matchResult!!.destructured

                return BbsInfo(
                    protocol = protocol,
                    domain = domain,
                    bbs = bbs,
                    key = key
                )
            } else if (url.isBoard()) {
                val matchResult = Regex("""^(https?://)([^/]+)/([^/]+)/$""").find(url)
                val (protocol, domain, bbs) = matchResult!!.destructured

                return BbsInfo(
                    protocol = protocol,
                    domain = domain,
                    bbs = bbs
                )
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}


object ChmateString {
    const val APP_NAME = "jp.co.airfront.android.a2chMate"
    const val ACTIVITY_NAME = "jp.syoboi.a2chMate.activity.ResEditActivity\$Dialog"
    const val EXTRAS_URL = "sourceUrl"
    const val EXTRAS_TITLE = "sourceTitle"
    const val EXTRAS_1RES = "source1res"
}

