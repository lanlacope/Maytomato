package io.github.lanlacope.maytomato.clazz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import io.github.lanlacope.maytomato.wigit.appendNumCharRef

@Suppress("unused")
@Composable
fun rememberStringConverter(): StringConverter {
    return remember {
        StringConverter()
    }
}

object ConvertMode {
    const val ALL = "All"
    const val SELECTOR = "Selector"
    const val REMOVE = "Remove"
}

object ConvertNumber {
    const val DEC = 10
    const val HEX = 16
}

@Stable
class StringConverter {

    /*
     * 対象にしない
     *  モンゴル語の異体字セレクタ(U+180B ～ U+180D)
     * 対象にする
     *  絵文字の異体字セレクタ(U+FE00 ～ U+FE0F)
     *  漢字の異体字セレクタ(U+E0100 ～ U+E01EF)
     */
    private val isSelector: (Int) -> Boolean = { codePoint ->
        codePoint in 0xFE00..0xFE0F || codePoint in 0xE0100..0xE01EF
    }

    fun convert(
        rawText: String,
        mode: String = ConvertMode.SELECTOR,
        number: Int = ConvertNumber.DEC
    ): String {
        return buildString {

            var index = 0
            while (index < rawText.length) {
                val codePoint = rawText.codePointAt(index)

                if (isSelector(codePoint)) {
                    when (mode) {
                        ConvertMode.REMOVE -> { /* do nothing */ }
                        else -> appendNumCharRef(codePoint, number)
                    }
                }
                else {
                    when (mode) {
                        ConvertMode.ALL -> appendNumCharRef(codePoint, number)
                        else -> appendCodePoint(codePoint)
                    }
                }

                // サロゲートペアの場合は2つ進める
                index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
            }
        }
    }
}