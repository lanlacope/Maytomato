package io.github.lanlacope.maytomato.clazz

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.collection.json.forEach
import io.github.lanlacope.collection.json.keyList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
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

            val enabled = data.optBoolean(BoardSettingJson.ENABLED, true)
            val useAgent = data.optString(BoardSettingJson.USER_AGENT,
                "Monazilla/1.00 Maytomato"
                    + " (Linux; U; Android ${Build.VERSION.RELEASE}; "
                    + "${Build.MODEL} Build/${Build.ID})"
            )
            val formOrder = data.optString(
                BoardSettingJson.FORM_ORDER,
                PostQuery.entries.toList().joinToString()
            ).split(", ").map { PostQuery.fromString(it)}.toPersistentList()
            val usedMobileCommunication = data.optBoolean(BoardSettingJson.USED_MOBILE_COMMUNICATION, false)
            val unusedClearTraffic = data.optBoolean(BoardSettingJson.UNUSED_CLEAR_TRAFFIC, false)

            boardList.add(
                BoardSetting(
                    domain = domain,
                    enabled = enabled,
                    useAgent = useAgent,
                    formOrder =  formOrder,
                    usedMobileCommunication = usedMobileCommunication,
                    unusedClearTraffic = unusedClearTraffic
                )
            )
        }
        return@withContext boardList
    }

    suspend fun addBoard(
        boardSetting: BoardSetting
    ): Result<BoardSetting> = withContext(Dispatchers.IO) {

        val boardFile = appContext.getCopipeListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (boardObject.keyList().contains(boardSetting.domain)) {
            return@withContext Result.failure(Exception())
        }

        val data = boardSetting.toJSONObject()

        boardObject.put(boardSetting.domain, data)
        boardFile.writeText(boardObject.toString())

        return@withContext Result.success(boardSetting)
    }

    suspend fun editBoard(
        boardSetting: BoardSetting
    ): Result<BoardSetting>  = withContext(Dispatchers.IO) {

        val boardFile = appContext.getBoardListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (boardObject.keyList().contains(boardSetting.domain)) {
            return@withContext Result.failure(Exception())
        }

        val data = boardSetting.toJSONObject()

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

    suspend fun checkBoardEnabled(domain: String): Boolean = withContext(Dispatchers.IO) {
        val boardFile = appContext.getBoardListFile()

        val boardObject = try {
            JSONObject(boardFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        boardObject.keyList().forEach { key ->
            if (domain.endsWith(key)) {
                if (boardObject.getJSONObject(key).getBoolean(BoardSettingJson.ENABLED)) {
                    return@withContext true
                }
            }
        }
        return@withContext false
    }
}
private object BoardSettingJson {
    const val ENABLED = "Enabled"
    const val USER_AGENT = "UserAgent"
    const val FORM_ORDER = "FormOrder"
    const val USED_MOBILE_COMMUNICATION = "UsedMobileCommunication"
    const val UNUSED_CLEAR_TRAFFIC = "UnusedClearTraffic"
}

data class BoardSetting(
    val domain: String,
    val enabled: Boolean,
    val useAgent: String,
    val formOrder: PersistentList<PostQuery>,
    val usedMobileCommunication: Boolean,
    val unusedClearTraffic: Boolean
) {
    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            val data = JSONObject().apply {
                put(BoardSettingJson.ENABLED, enabled)
                put(BoardSettingJson.USER_AGENT, useAgent)
                put(BoardSettingJson.FORM_ORDER, formOrder.joinToString())
                put(BoardSettingJson.USED_MOBILE_COMMUNICATION, usedMobileCommunication)
                put(BoardSettingJson.UNUSED_CLEAR_TRAFFIC, unusedClearTraffic)
            }
            put(domain, data)
        }
    }

    companion object {
        fun fromJSONObject(domain: String, jsonObject: JSONObject): BoardSetting {
            jsonObject.apply {
                val enabled = optBoolean(BoardSettingJson.ENABLED, true)
                val useAgent = optString(
                    BoardSettingJson.USER_AGENT,
                    "Monazilla/1.00 Maytomato"
                            + " (Linux; U; Android ${Build.VERSION.RELEASE}; "
                            + "${Build.MODEL} Build/${Build.ID})"
                )
                val formOrder = optString(
                    BoardSettingJson.FORM_ORDER,
                    PostQuery.entries.toList().joinToString()
                ).split(", ").map { PostQuery.fromString(it) }.toPersistentList()
                val usedMobileCommunication =
                    optBoolean(BoardSettingJson.USED_MOBILE_COMMUNICATION, false)
                val unusedClearTraffic =
                    optBoolean(BoardSettingJson.UNUSED_CLEAR_TRAFFIC, false)

                return BoardSetting(
                    domain = domain,
                    enabled = enabled,
                    useAgent = useAgent,
                    formOrder = formOrder,
                    usedMobileCommunication = usedMobileCommunication,
                    unusedClearTraffic = unusedClearTraffic
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