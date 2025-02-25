package io.github.lanlacope.maytomato

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.lanlacope.collection.json.forEach
import io.github.lanlacope.maytomato.clazz.CopipeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

/*
 * 1.xxから2.00へのデータ移行用クラス
 * 2.1.xで削除
 *
 */

@Composable
fun rememberTo200Helper(): To200Helper {
    val context = LocalContext.current
    return To200Helper(context)
}

class To200Helper(context: Context) {

    val appContext = context.applicationContext
    val copipeManager = CopipeManager(appContext)

    private val DATA_DIR_NAME = "data"
    private val COPIPE_LIST_FILE_NAME = "copipe.json"
    private val AA_LIST_FILE_NAME = "aa.json"
    private val COMMAND_LIST_FILE_NAME = "command.json"


    private fun Context.getPreAppDataFilesDir(): File {
        val folder = File(getExternalFilesDir(null), DATA_DIR_NAME)
        return folder
    }

    fun Context.getPreCopipeListFile(): File {
        val file = File(getPreAppDataFilesDir(), COPIPE_LIST_FILE_NAME)
        return file
    }

    fun Context.getPreAaListFile(): File {
        val file = File(getPreAppDataFilesDir(), AA_LIST_FILE_NAME)
        return file
    }

    fun Context.getPreCommandListFile(): File {
        val file = File(getPreAppDataFilesDir(), COMMAND_LIST_FILE_NAME)
        return file
    }

    suspend fun to2xxData() = withContext(Dispatchers.IO) {

        if (!appContext.getPreAppDataFilesDir().exists()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(appContext, "1.xxのデータが見つかりませんでした", Toast.LENGTH_SHORT).show()
            }
            return@withContext
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, "データ移行開始、しばらく待機してください", Toast.LENGTH_LONG).show()
        }

        var isFailed = false

        val copipeObject = try {
            JSONObject(appContext.getPreCopipeListFile().readText())
        } catch (e: Exception) {
            JSONObject()
        }
        val aaObject = try {
            JSONObject(appContext.getPreAaListFile().readText())
        } catch (e: Exception) {
            JSONObject()
        }
        val commandObject = try {
            JSONObject(appContext.getPreCommandListFile().readText())
        } catch (e: Exception) {
            JSONObject()
        }

        copipeObject.forEach { title: String, text: String ->
            try {
                copipeManager.addCopipe(title, text)
            } catch (e: Exception) {
                isFailed = true
            }
        }
        aaObject.forEach { title: String, text: String ->
            try {
                copipeManager.addAa(title, text)
            } catch (e: Exception) {
                isFailed = true
            }
            copipeManager.addAa(title, text)
        }
        commandObject.forEach { title: String, text: String ->
            try {
                copipeManager.addCommand(title, text)
            } catch (e: Exception) {
                isFailed = true
            }
        }

        if (isFailed) {
            withContext(Dispatchers.Main) {
                Toast.makeText(appContext, "データ移行完了、一部欠損あり", Toast.LENGTH_LONG).show()
            }
        }
        else {
            withContext(Dispatchers.Main) {
                Toast.makeText(appContext, "データ移行完了", Toast.LENGTH_LONG).show()
            }
        }

        appContext.getPreAppDataFilesDir().deleteRecursively()
    }
}