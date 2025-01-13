package io.github.lanlacope.maytomato.clazz

import android.content.Context
import java.io.File

private const val DATA_DIR_NAME = "data"
private const val COPIPE_DIR_NAME = "copipe"
private const val COPIPE_LIST_FILE_NAME = "copipe.json"
private const val AA_LIST_FILE_NAME = "aa.json"
private const val COMMAND_LIST_FILE_NAME = "command.json"


private fun Context.getAppDataFilesDir(): File {
    val folder = File(getExternalFilesDir(null), DATA_DIR_NAME)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder
}

fun Context.getCopipeListDir(): File {
    val folder = File(getAppDataFilesDir(), COPIPE_DIR_NAME)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder
}

fun Context.getCopipeListFile(): File {
    val file = File(getAppDataFilesDir(), COPIPE_LIST_FILE_NAME)
    file.createNewFile()
    return file
}

fun Context.getAaListFile(): File {
    val file = File(getAppDataFilesDir(), AA_LIST_FILE_NAME)
    file.createNewFile()
    return file
}

fun Context.getCommandListFile(): File {
    val file = File(getAppDataFilesDir(), COMMAND_LIST_FILE_NAME)
    file.createNewFile()
    return file
}
