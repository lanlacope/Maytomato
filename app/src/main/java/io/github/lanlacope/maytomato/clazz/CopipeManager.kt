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

        copipeObject.forEach { title: String, text: String ->
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

        if (copipeObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        copipeObject.put(title, text)
        copopeFile.writeText(copipeObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editCopipe(title: String, text: String, lastTitle: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val copopeFile = appContext.getCopipeListFile()

        val copipeObject = try {
            JSONObject(copopeFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (copipeObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        copipeObject.remove(lastTitle)
        copipeObject.put(title, text)
        copopeFile.writeText(copipeObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeCopipe(title: String) = withContext(Dispatchers.IO) {
        val copopeFile = appContext.getCopipeListFile()

        val copipeObject = try {
            JSONObject(copopeFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        copipeObject.remove(title)
        copopeFile.writeText(copipeObject.toString())
    }

    suspend fun getAaList(): List<CopipeData> = withContext(Dispatchers.IO) {
        val aaObject = try {
            JSONObject(appContext.getAaListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val aaList = emptyList<CopipeData>().toMutableList()

        aaObject.forEach { title: String, text: String ->
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

        if (aaObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        aaObject.put(title, text)
        aaFile.writeText(aaObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editAa(title: String, text: String, lastTitle: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val aaFile = appContext.getAaListFile()

        val aaObject = try {
            JSONObject(aaFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (aaObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        aaObject.remove(lastTitle)
        aaObject.put(title, text)
        aaFile.writeText(aaObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeAa(title: String) = withContext(Dispatchers.IO) {
        val aaFile = appContext.getAaListFile()

        val aaObject = try {
            JSONObject(aaFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        aaObject.remove(title)
        aaFile.writeText(aaObject.toString())
    }

    suspend fun getCommandList(): List<CopipeData> = withContext(Dispatchers.IO) {
        val commandObject = try {
            JSONObject(appContext.getCommandListFile().readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        val commandList = emptyList<CopipeData>().toMutableList()

        commandObject.forEach { title: String, text: String ->
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

        if (commandObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        commandObject.put(title, text)
        commandFile.writeText(commandObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun editCommand(title: String, text: String, lastTitle: String): Result<CopipeData>  = withContext(Dispatchers.IO) {

        val commandFile = appContext.getCommandListFile()

        val commandObject = try {
            JSONObject(commandFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        if (commandObject.keyList().contains(title)) {
            return@withContext Result.failure(Exception())
        }

        commandObject.remove(lastTitle)
        commandObject.put(title, text)
        commandFile.writeText(commandObject.toString())

        return@withContext Result.success(CopipeData(title, text))
    }

    suspend fun removeCommand(title: String) = withContext(Dispatchers.IO) {
        val commandFile = appContext.getCommandListFile()

        val commandObject = try {
            JSONObject(commandFile.readText())
        } catch (e: JSONException) {
            JSONObject()
        }

        commandObject.remove(title)
        commandFile.writeText(commandObject.toString())
    }
}

data class CopipeData(
    val title: String,
    val text: String
)