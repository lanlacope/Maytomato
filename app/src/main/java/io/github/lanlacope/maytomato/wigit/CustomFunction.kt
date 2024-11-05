package io.github.lanlacope.maytomato.wigit

import android.util.Log

fun StringBuilder.appendNumCharRef(
    codePoint: Int,
    num: Int
) {
    when (num) {
        10 -> append("&#").append(codePoint.toString(10)).append(";")
        16 -> append("&#x").append(codePoint.toString(16)).append(";")
        else -> Log.w("StringBuilder", "$num is not supported number!")
    }
}
