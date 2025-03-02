package io.github.lanlacope.maytomato.clazz

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.maytomato.R
import io.github.lanlacope.maytomato.activity.BbsInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream


@Suppress("unused")
@Composable
fun rememberBbsPostClient(bbsInfo: BbsInfo, bbsSetting: BoardSetting): BbsPostClient {
    val context = LocalContext.current
    return remember {
        BbsPostClient(context, bbsInfo, bbsSetting)
    }
}

class BbsPostClient(
    private val context: Context,
    private val bbsInfo: BbsInfo,
    private val bbsSetting: BoardSetting,
) {

    suspend fun sendPost(
        name: String,
        mail: String,
        subject: String,
        message: String,
        onSucces: (resNum: Int?) -> Unit,
        onFailed: (title: String, text: String) -> Unit
    ) = withContext(Dispatchers.IO) {

        val cookieManager = CookieManager(context)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val encodeStr = "shift_jis"
            val encodeChar = Charset.forName(encodeStr)
            val requestURL = if (bbsSetting.forceClearHttps) {
                "https://${bbsInfo.domain}/test/bbs.cgi"
            } else {
                "${bbsInfo.protocol}${bbsInfo.domain}/test/bbs.cgi"
            }


        val cockie = cookieManager.getCookie(bbsSetting.domain)

        val connection = URL(requestURL).openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "POST"
            doInput = true
            doOutput = true
            useCaches = false
            connectTimeout = 10000
            setRequestProperty("Host", bbsInfo.domain)
            setRequestProperty("Connection", "keep-alive")
            setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded; charset=$encodeStr"
            )
            setRequestProperty("Accept-Charset", encodeStr)
            setRequestProperty("Accept-Encoding", "gzip, identity")
            setRequestProperty(
                "Referer",
                "${bbsInfo.protocol}${bbsInfo.domain}/${bbsInfo.bbs}/${bbsInfo.key}/"
            )
            setRequestProperty("User-Agent", bbsSetting.userAgent)
            if (cockie.isNotEmpty()) {
                setRequestProperty("Cookie", cockie)
            }
        }


        try {
            // モバイル通信を強制
            if (bbsSetting.forceMobileCommunication) {
                if (!connectivityManager.bindMobileCommunication()) {
                    onFailed(context.getString(R.string.dialog_failed_title_connection), context.getString(R.string.dialog_failed_text_mobile))
                }
            }

            connection.outputStream.use { it.write(createByteCode(name, mail, subject, message)) }

            val responseCode = connection.responseCode

            /*
            connection.headerFields.forEach { (key, values) ->
                Log.d("PostingProcess", "$key: ${values.joinToString(", ")}")
            }

             */

            if (connection.getCookie().isNotEmpty()) {
                cookieManager.updateCookie(bbsSetting.domain, connection.getCookie())
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream =
                    when (connection.getHeaderField("Content-Encoding")?.lowercase()) {
                        "gzip" -> GZIPInputStream(connection.inputStream)
                        "deflate" -> InflaterInputStream(connection.inputStream, Inflater(true))
                        else -> connection.inputStream
                    }

                val responce = inputStream.bufferedReader(encodeChar).use { it.readText() }
                println(responce)

                val result = PostResult.parse(responce)

                if (result.isSuccess) {
                    val resNum = connection.getHeaderField("X-ResNum")?.toIntOrNull()
                    onSucces(resNum)
                }
                else {
                    onFailed(result.title, result.text)
                }
            } else {
                onFailed(context.getString(R.string.dialog_failed_title_connection), responseCode.toString())
            }

        } catch (e: SocketTimeoutException){
            onFailed(context.getString(R.string.dialog_failed_title_connection), context.getString(R.string.dialog_failed_text_timeout))
        } catch (e: Exception) {
            onFailed(context.getString(R.string.dialog_failed_title_unknown), "$e : ${e.message}")
        } finally {
            connection.disconnect()

            // モバイル通信を強制の解除
            if (bbsSetting.forceMobileCommunication) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(null)
                } else {
                    @Suppress("DEPRECATION")
                    ConnectivityManager.setProcessDefaultNetwork(null)
                }
            }
        }
    }

    private fun createByteCode(name: String, mail: String, subject: String, message: String): ByteArray {
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
            "bbs=${bbsInfo.bbs}&subject=$subjectJis&time=$currentTime&FROM=$nameJis&mail=$mailJis&MESSAGE=$messageJis&submit=$submit".toByteArray(
                Charset.forName("shift_JIS")
            )
        } else {
            "bbs=${bbsInfo.bbs}&key=${bbsInfo.key}&time=$currentTime&FROM=$nameJis&mail=$mailJis&MESSAGE=$messageJis&submit=$submit".toByteArray(
                Charset.forName("shift_JIS")
            )
        }
    }

    private fun String.urlEncodeWithJis(): String {
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

    private fun HttpURLConnection.getCookie(): String {
        val headers = this.headerFields
        val cookies = headers["Set-Cookie"]
        return buildString {
            cookies?.forEach { cookie ->
                val matchResult = Regex("""(.+?)=(.*)""").find(cookie)
                if (matchResult != null) {
                    val (key, value) = matchResult.destructured
                    // これらは保存しない
                    if (key.lowercase() !in listOf("expires", "path", "secure", "name", "mail")) {
                        append("$key=$value; ")
                    }
                }
            } ?: ""
        }.removeSuffix("; ")
    }

    private data class PostResult(
        val title: String,
        val text: String,
        val isSuccess: Boolean
    ) {
        companion object {
            fun parse(response: String): PostResult {
                val matcher = Regex(
                    """<title>(.*?)</title>.*?<body>(.*?)</body>""",
                    setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
                ).find(response)

                val title = matcher?.groups?.get(1)?.value?.replace(
                    Regex(
                        """<br>""",
                        RegexOption.IGNORE_CASE
                    ), "\n"
                )?.replace(Regex("""<[^<>]*>""", RegexOption.DOT_MATCHES_ALL), "") ?: ""

                val text = matcher?.groups?.get(2)?.value?.replace(
                    Regex(
                        """<br>""",
                        RegexOption.IGNORE_CASE
                    ), "\n"
                )?.replace(Regex("""<[^<>]*>""", RegexOption.DOT_MATCHES_ALL), "") ?: ""


                return when {
                    response.contains(Regex("""<!--\s*2ch_X:true\s*-->""", RegexOption.IGNORE_CASE)) -> PostResult(title, text, true)
                    response.contains(Regex("""<!--\s*2ch_X:error\s*-->""", RegexOption.IGNORE_CASE)) -> PostResult(title, text, false)
                    title.contains(
                        Regex("ERORR", RegexOption.IGNORE_CASE)
                    ) -> PostResult(title, text, false)

                    title.contains(
                        Regex("""書き[込こ]み(ました|完了|成功|OK|ＯＫ)""")
                    ) -> PostResult(title, text, true)

                    else -> PostResult(title, text, false)
                }
            }
        }
    }
}