package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.rewheel.util.json.forEach
import io.github.lanlacope.rewheel.util.json.toKeyList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

@Suppress("unused")
@Composable
fun rememberBoardManager(): BoardManager {
    val context = LocalContext.current
    return remember {
        BoardManager(context)
    }
}

class BoardManager(context: Context) {
    private val appContext = context.applicationContext

    suspend fun getBoardList(): List<BoardSetting> = withContext(Dispatchers.IO) {
        val boardObject = try {
            JSONObject(appContext.getBoardListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val boardList = emptyList<BoardSetting>().toMutableList()

        boardObject.forEach { domain, data ->
            boardList.add(
                BoardSetting.fromJSONObject(domain, data as JSONObject)
            )
        }
        return@withContext boardList
    }

    suspend fun addBoard(
        domain: String
    ): Result<BoardSetting> = withContext(Dispatchers.IO) {

        val boardFile = appContext.getBoardListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (boardObject.toKeyList().contains(domain)) {
            return@withContext Result.failure(Exception())
        }

        boardObject.put(domain, JSONObject())
        boardFile.writeText(boardObject.toString())

        return@withContext Result.success(BoardSetting.fromJSONObject(domain, JSONObject()))
    }

    suspend fun editBoard(
        boardSetting: BoardSetting,
    ): Result<BoardSetting>  = withContext(Dispatchers.IO) {

        val boardFile = appContext.getBoardListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }


        val data = JSONObject().apply {
            put(BoardSettingJson.ENABLED, boardSetting.enabled)
            put(BoardSettingJson.USER_AGENT, boardSetting.userAgent)
            put(BoardSettingJson.REMOVE_MAIL, boardSetting.removeMail)
            put(BoardSettingJson.FORCE_MOBILE_COMMUNICATION, boardSetting.forceMobileCommunication)
            put(BoardSettingJson.FORCE_CLEAR_TRAFFIC, boardSetting.forceClearHttps)
        }

        boardObject.put(boardSetting.domain, data)
        boardFile.writeText(boardObject.toString())

        return@withContext Result.success(boardSetting)
    }

    suspend fun removeBoard(domain: String) = withContext(Dispatchers.IO) {
        val boardFile = appContext.getBoardListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        boardObject.remove(domain)
        boardFile.writeText(boardObject.toString())
    }
}
private object BoardSettingJson {
    const val ENABLED = "Enabled"
    const val USER_AGENT = "UserAgent"
    const val REMOVE_MAIL = "RemoveMail"
    const val FORCE_MOBILE_COMMUNICATION = "ForceMobileCommunication"
    const val FORCE_CLEAR_TRAFFIC = "ForceHttps"
}

data class BoardSetting(
    val domain: String,
    val enabled: Boolean,
    val userAgent: String,
    val removeMail: Boolean,
    val forceMobileCommunication: Boolean,
    val forceClearHttps: Boolean
) {

    companion object {
        fun fromJSONObject(domain: String, jsonObject: JSONObject): BoardSetting {
            jsonObject.apply {
                val enabled = optBoolean(BoardSettingJson.ENABLED, true)
                val useAgent = optString(BoardSettingJson.USER_AGENT,"")
                val removeMail = optBoolean(BoardSettingJson.REMOVE_MAIL, false)
                val forceMobileCommunication = optBoolean(BoardSettingJson.FORCE_MOBILE_COMMUNICATION, false)
                val forceClearTraffic = optBoolean(BoardSettingJson.FORCE_CLEAR_TRAFFIC, true)

                return BoardSetting(
                    domain = domain,
                    enabled = enabled,
                    userAgent = useAgent,
                    removeMail = removeMail,
                    forceMobileCommunication = forceMobileCommunication,
                    forceClearHttps = forceClearTraffic
                )
            }
        }
    }
}

enum class PostQuery {
    BBS, KEY, SUBJECT, FROM, MAIL, MESSAGE, SUBMIT;

    override fun toString(): String {
        return when (this) {
            BBS -> "bbs"
            KEY -> "key"
            SUBJECT -> "subject"
            FROM -> "FROM"
            MAIL -> "mail"
            MESSAGE -> "MESSAGE"
            SUBMIT -> "submit"
        }
    }

    companion object {
        fun fromString(value: String): PostQuery {
            return entries.find { it.toString() == value } ?: SUBMIT
        }
    }
}