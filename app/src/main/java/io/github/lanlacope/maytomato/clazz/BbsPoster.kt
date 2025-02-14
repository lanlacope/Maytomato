package io.github.lanlacope.maytomato.clazz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.lanlacope.maytomato.activity.BbsInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import javax.security.auth.Subject

@Suppress("unused")
@Composable
fun rememberBbsPoster(bbsInfo: BbsInfo, bbsSetting: BoardSetting): BbsPoster {
    return remember {
        BbsPoster(bbsInfo, bbsSetting)
    }
}

class BbsPoster(
    private val bbsInfo: BbsInfo,
    private val bbsSetting: BoardSetting,
) {

    suspend fun sendPost(
        name: String,
        mail: String,
        subject: String,
        message: String,
        onSucces: (String) -> Unit,
        onFailed: () -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val encodeStr = "shift_jis"
            val encodeChar = Charset.forName(encodeStr)
            val requestURL = if (true) {
                "${bbsInfo.protocol}${bbsInfo.domain}/test/bbs.cgi"
            } else {
                "http://${bbsInfo.domain}/test/bbs.cgi"
            }


            val cockie: String? = null

            val connection = URL(requestURL).openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "POST"
                doInput = true
                doOutput = true
                useCaches = false
                connectTimeout = 30
                setRequestProperty("Host", bbsInfo.domain)
                setRequestProperty("Connection", "keep-alive")
                setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=$encodeStr"
                )
                setRequestProperty("Accept-Charset", encodeStr)
                setRequestProperty("Accept-Encoding", "gzip, identity")
                setRequestProperty("Referer", "${bbsInfo.protocol}${bbsInfo.domain}/${bbsInfo.bbs}/${bbsInfo.key}/")
                setRequestProperty("User-Agent", bbsSetting.useAgent)
                cockie?.let { setRequestProperty("Cookie", it) }
            }

            connection.outputStream.use { it.write(createByteCode(name, mail, subject, message)) }

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream =
                    when (connection.getHeaderField("Content-Encoding")?.lowercase()) {
                        "gzip" -> GZIPInputStream(connection.inputStream)
                        "deflate" -> InflaterInputStream(connection.inputStream, Inflater(true))
                        else -> connection.inputStream
                    }

                onSucces(inputStream.bufferedReader(encodeChar).use { it.readText() })
            } else {
                onFailed()
            }

            connection.getCookie() // TODO: 保存
            connection.disconnect()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createByteCode(name: String, mail: String, subject: String, message: String): ByteArray {
        val currentTime = (System.currentTimeMillis() / 1000L).toString()
        val nameJis = name.urlEncodeWithJis()
        val mailJis = mail.urlEncodeWithJis()
        val subjectJis = subject.urlEncodeWithJis()
        val messageJis = message.urlEncodeWithJis()
        val submit = if (bbsInfo.key.isNullOrEmpty()) {
            URLEncoder.encode("新規スレッド作成", "Shift_JIS")
        } else {
            URLEncoder.encode("書き込む", "Shift_JIS")
        }
        return if (bbsInfo.key.isNullOrEmpty()) {
            "bbs=${bbsInfo.bbs}&key=${bbsInfo.key}&time=$currentTime&FROM=$nameJis&mail=$mailJis&MESSAGE=$messageJis&submit=$submit".toByteArray(Charset.forName("shift_JIS"))
        } else {
            "bbs=${bbsInfo.bbs}&subject=$subjectJis&time=$currentTime&FROM=$nameJis&mail=$mailJis&MESSAGE=$messageJis&submit=$submit".toByteArray(Charset.forName("shift_JIS"))
        }
    }

    fun String.urlEncodeWithJis(): String {
        return buildString {
            this@urlEncodeWithJis.forEach { c ->
                val bytes = c.toString().toByteArray(Charset.forName("Shift_JIS"))
                val encodedChar = if (String(bytes, Charset.forName("Shift_JIS")) == c.toString()) {
                    URLEncoder.encode(c.toString(), "Shift_JIS")
                } else {
                    // 文字化けしたら文字を10進数の数値参照に置き換える
                    URLEncoder.encode("&#${c.code};", "Shift_JIS")
                }
                append(encodedChar)
            }
        }.replace("+", "%20")
    }

    fun HttpURLConnection.getCookie(): String {
        val headers = this.headerFields
        val cookies = headers["Set-Cookie"]
        return buildString {
            cookies?.forEach { cookie ->
                val matchResult = Regex("""(.+?)=(.*)""").find(cookie)
                if (matchResult != null) {
                    val (key, value) = matchResult.destructured
                    val lowerKey = key.lowercase()

                    // これらは保存しない
                    if (lowerKey !in listOf("expires", "path", "secure", "name", "mail")) {
                        append("$key=$value; ")
                    }
                }
            } ?: ""
        }.removeSuffix("; ")
    }
}