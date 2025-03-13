package io.github.lanlacope.maytomato.clazz

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.rewheel.util.json.forEach
import io.github.lanlacope.rewheel.util.json.keyList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

@Suppress("unused")
@Composable
fun rememberCopipeManager(): CopipeManager {
    val context = LocalContext.current
    return remember {
        CopipeManager(context)
    }
}

class CopipeManager(context: Context) {
    private val appContext = context.applicationContext

    suspend fun getCopipeList(): List<CopipeData> = withContext(Dispatchers.IO) {
        val copipeObject = try {
            JSONObject(appContext.getCopipeListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val copipeList = emptyList<CopipeData>().toMutableList()

        copipeObject.forEach { text: String, data: JSONObject ->
            val title = data.optString(CopipeJson.TITLE)
            copipeList.add(CopipeData(title, text))
        }
        return@withContext copipeList
    }

    suspend fun addCopipe(title: String, text: String): Result<CopipeData> = withContext(Dispatchers.IO) {

        val copopeFile = appContext.getCopipeListFile()

        val copipeObject = try {
            JSONObject(copopeFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (copipeObject.keyList().contains(text)) {
            return@withContext Result.failure(Exception())
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }

        copipeObject.put(text, data)
        copopeFile.writeText(copipeObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editCopipe(title: String, text: String, lastText: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val copopeFile = appContext.getCopipeListFile()

        val copipeObject = try {
            JSONObject(copopeFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (text != lastText) {
            if (copipeObject.keyList().contains(text)) {
                return@withContext Result.failure(Exception())
            }
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }
        copipeObject.remove(lastText)
        copipeObject.put(text, data)

        copopeFile.writeText(copipeObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeCopipe(text: String) = withContext(Dispatchers.IO) {
        val copopeFile = appContext.getCopipeListFile()

        val copipeObject = try {
            JSONObject(copopeFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        copipeObject.remove(text)
        copopeFile.writeText(copipeObject.toString())
    }

    suspend fun getAaList(): List<CopipeData> = withContext(Dispatchers.IO) {
        val aaObject = try {
            JSONObject(appContext.getAaListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val aaList = emptyList<CopipeData>().toMutableList()

        aaObject.forEach { text: String, data: JSONObject ->
            val title = data.optString(CopipeJson.TITLE)
            aaList.add(CopipeData(title, text))
        }
        return@withContext aaList
    }

    suspend fun addAa(title: String, text: String): Result<CopipeData> = withContext(Dispatchers.IO) {

        val aaFile = appContext.getAaListFile()

        val aaObject = try {
            JSONObject(aaFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (aaObject.keyList().contains(text)) {
            return@withContext Result.failure(Exception())
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }

        aaObject.put(text, data)
        aaFile.writeText(aaObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editAa(title: String, text: String, lastText: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val aaFile = appContext.getAaListFile()

        val aaObject = try {
            JSONObject(aaFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (text != lastText) {
            if (aaObject.keyList().contains(text)) {
                return@withContext Result.failure(Exception())
            }
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }
        aaObject.remove(lastText)
        aaObject.put(text, data)
        aaFile.writeText(aaObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeAa(text: String) = withContext(Dispatchers.IO) {
        val aaFile = appContext.getAaListFile()

        val aaObject = try {
            JSONObject(aaFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        aaObject.remove(text)
        aaFile.writeText(aaObject.toString())
    }

    suspend fun getCommandList(): List<CopipeData> = withContext(Dispatchers.IO) {
        val commandObject = try {
            JSONObject(appContext.getCommandListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val commandList = emptyList<CopipeData>().toMutableList()

        commandObject.forEach { text: String, data: JSONObject ->
            val title = data.optString(CopipeJson.TITLE)
            commandList.add(CopipeData(title, text))
        }
        return@withContext commandList
    }

    suspend fun addCommand(title: String, text: String): Result<CopipeData> = withContext(Dispatchers.IO) {

        val commandFile = appContext.getCommandListFile()

        val commandObject = try {
            JSONObject(commandFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (commandObject.keyList().contains(text)) {
            return@withContext Result.failure(Exception())
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }

        commandObject.put(text, data)
        commandFile.writeText(commandObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editCommand(title: String, text: String, lastText: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val commandFile = appContext.getCommandListFile()

        val commandObject = try {
            JSONObject(commandFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (text != lastText) {
            if (commandObject.keyList().contains(text)) {
                return@withContext Result.failure(Exception())
            }
        }

        val data = JSONObject().apply {
            put(CopipeJson.TITLE, title)
        }
        commandObject.remove(lastText)
        commandObject.put(text, data)
        commandFile.writeText(commandObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeCommand(text: String) = withContext(Dispatchers.IO) {
        val commandFile = appContext.getCommandListFile()

        val commandObject = try {
            JSONObject(commandFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        commandObject.remove(text)
        commandFile.writeText(commandObject.toString())
    }
}

private object CopipeJson {
    const val TITLE = "Title"
}


data class CopipeData(
    val title: String,
    val text: String
)