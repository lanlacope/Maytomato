package io.github.lanlacope.maytomato.clazz

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Suppress("unused")
@Composable
fun rememberStringConverter(): StringConverter {
    return remember {
        StringConverter()
    }
}

object ConvertMode {
    const val ALL = "All"
    const val SKIP_BR = "SlipBreak"
    const val MOJIBAKE = "SelectorHigh"
    const val REMOVE = "Remove"
}

object ConvertNumber {
    const val DEC = 10
    const val HEX = 16
}

@Stable
class StringConverter {

    /*
     * ゼロ幅文字を判定
     * モンゴル語の異体字セレクタ(U+180B ～ U+180D),
     * 絵文字の異体字セレクタ(U+FE00 ～ U+FE0F),
     * 漢字の異体字セレクタ(U+E0100 ～ U+E01EF),
     * ゼロ幅空白(U+200B),ゼロ幅非結合子(U+200C),ゼロ幅結合子(U+200D),
     * 方向指定子(U+200E ～ U+200F),(U+202A ～ U+202E)
     */
    private val isZeroWidth: (Int) -> Boolean = { codePoint ->
        codePoint in 0x180B..0x180D || codePoint in 0xFE00..0xFE0F || codePoint in 0xE0100..0xE01EF
                || codePoint in  0x200B..0x200F || codePoint in  0x202A..0x202E
    }

    /*
     * 改行を判定
     */
    private val isBreak: (Int) -> Boolean = { codePoint ->
        codePoint == 0xA
    }


    private fun StringBuilder.appendNumCharRef(
        codePoint: Int,
        num: Int
    ) {
        when (num) {
            10 -> append("&#").append(codePoint.toString(10)).append(";")
            16 -> append("&#x").append(codePoint.toString(16)).append(";")
            else -> Log.w("StringBuilder", "$num is not supported number!")
        }
    }


    fun convert(
        rawText: String,
        mode: String = ConvertMode.MOJIBAKE,
        number: Int = ConvertNumber.DEC
    ): String {
        return buildString {

            var index = 0
            while (index < rawText.length) {
                val codePoint = rawText.codePointAt(index)

                if (isZeroWidth(codePoint)) {
                    when (mode) {
                        ConvertMode.MOJIBAKE -> {
                            appendNumCharRef(codePoint, number)
                        }
                        ConvertMode.REMOVE -> { /* do nothing */ }
                        else -> appendNumCharRef(codePoint, number)
                    }
                }
                else {
                    when (mode) {
                        ConvertMode.ALL -> appendNumCharRef(codePoint, number)
                        ConvertMode.SKIP_BR -> {
                            if (!isBreak(codePoint)) {
                                appendNumCharRef(codePoint, number)
                            }
                            else {
                                appendCodePoint(codePoint)
                            }
                        }
                        else -> appendCodePoint(codePoint)
                    }
                }

                // サロゲートペアの場合は下位文字の分も進める
                index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
            }
        }
    }
}