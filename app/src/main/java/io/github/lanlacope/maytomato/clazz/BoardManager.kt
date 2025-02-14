package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.collection.json.forEach
import io.github.lanlacope.collection.json.keyList
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

        boardObject.forEach { domain: String, data: JSONObject ->
            boardList.add(
                BoardSetting.fromJSONObject(domain, data)
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

        if (boardObject.keyList().contains(domain)) {
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
            put(BoardSettingJson.USER_AGENT, boardSetting.useAgent)
            put(BoardSettingJson.USED_MOBILE_COMMUNICATION, boardSetting.usedMobileCommunication)
            put(BoardSettingJson.UNFORCED_CLEAR_TRAFFIC, boardSetting.unforcedClearTraffic)
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
    const val USED_MOBILE_COMMUNICATION = "UsedMobileCommunication"
    const val UNFORCED_CLEAR_TRAFFIC = "UnforcedClearTraffic"
}

data class BoardSetting(
    val domain: String,
    val enabled: Boolean,
    val useAgent: String,
    val usedMobileCommunication: Boolean,
    val unforcedClearTraffic: Boolean
) {

    companion object {
        fun fromJSONObject(domain: String, jsonObject: JSONObject): BoardSetting {
            jsonObject.apply {
                val enabled = optBoolean(BoardSettingJson.ENABLED, true)
                val useAgent = optString(BoardSettingJson.USER_AGENT,"")
                val usedMobileCommunication = optBoolean(BoardSettingJson.USED_MOBILE_COMMUNICATION, false)
                val unusedClearTraffic = optBoolean(BoardSettingJson.UNFORCED_CLEAR_TRAFFIC, false)

                return BoardSetting(
                    domain = domain,
                    enabled = enabled,
                    useAgent = useAgent,
                    usedMobileCommunication = usedMobileCommunication,
                    unforcedClearTraffic = unusedClearTraffic
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