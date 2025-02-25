package io.github.lanlacope.maytomato.clazz

import android.content.Context
import java.io.File

private const val APP_DIR_NAME = "maytomato"
private const val COPIPE_DIR_NAME = "copipe"
private const val COPIPE_LIST_FILE_NAME = "copipe_list.json"
private const val AA_LIST_FILE_NAME = "aa_list.json"
private const val COMMAND_LIST_FILE_NAME = "command_list.json"

private const val board_DIR_NAME = "board"
private const val BOARD_LIST_FILE_NAME = "board_list.json"


private fun Context.getAppDir(): File {
    val folder = File(getExternalFilesDir(null), APP_DIR_NAME)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder
}

private fun Context.getCopipeDir(): File {
    val folder = File(getAppDir(), COPIPE_DIR_NAME)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder
}

fun Context.getCopipeListFile(): File {
    val file = File(getCopipeDir(), COPIPE_LIST_FILE_NAME)
    file.createNewFile()
    return file
}

fun Context.getAaListFile(): File {
    val file = File(getCopipeDir(), AA_LIST_FILE_NAME)
    file.createNewFile()
    return file
}

fun Context.getCommandListFile(): File {
    val file = File(getCopipeDir(), COMMAND_LIST_FILE_NAME)
    file.createNewFile()
    return file
}

private fun Context.getBoardDir(): File {
    val folder = File(getExternalFilesDir(null), board_DIR_NAME)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder
}

fun Context.getBoardListFile(): File {
    val file = File(getBoardDir(), BOARD_LIST_FILE_NAME)
    file.createNewFile()
    return file
}
