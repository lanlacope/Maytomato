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
    const val SKIP_BR = "SlipBreak"
    const val SELECTOR_HIGH = "SelectorHigh"
    const val SELECTOR_LOW = "SelectorLow"
    const val REMOVE = "Remove"
}

object ConvertNumber {
    const val DEC = 10
    const val HEX = 16
}

@Stable
class StringConverter {

    /*
     * 異体字セレクタを判定
     * 対象にしない
     *  モンゴル語の異体字セレクタ(U+180B ～ U+180D)
     * 対象にする
     *  絵文字の異体字セレクタ(U+FE00 ～ U+FE0F)
     *  漢字の異体字セレクタ(U+E0100 ～ U+E01EF)
     */
    private val isSelector: (Int) -> Boolean = { codePoint ->
        codePoint in 0xFE00..0xFE0F || codePoint in 0xE0100..0xE01EF
    }

    /*
     * ゼロ幅結合子を判定
     */
    private val isZwj: (Int) -> Boolean = { codePoint ->
        codePoint == 0x200D
    }

    /*
     * 改行を判定
     */
    private val isBr: (Int) -> Boolean = { codePoint ->
        codePoint == 0xA
    }

    fun convert(
        rawText: String,
        mode: String = ConvertMode.SELECTOR_HIGH,
        number: Int = ConvertNumber.DEC
    ): String {
        return buildString {

            var index = 0
            while (index < rawText.length) {
                val codePoint = rawText.codePointAt(index)

                // サロゲートペアの場合は下位文字の分進める
                if (Character.isSupplementaryCodePoint(codePoint)) {
                    index += 1
                }

                if (isSelector(codePoint)) {
                    when (mode) {
                        ConvertMode.SELECTOR_HIGH -> {
                            appendNumCharRef(codePoint, number)

                            if (index + 1 < rawText.length) {
                                // 次の文字がゼロ幅結合子の場合はそれも変換
                                val nextPoint = rawText.codePointAt(index + 1)

                                if (isZwj(nextPoint)) {
                                    appendNumCharRef(nextPoint, number)
                                    // ゼロ幅結合子の分進める
                                    index += 1
                                }
                            }
                        }
                        ConvertMode.REMOVE -> { /* do nothing */ }
                        else -> appendNumCharRef(codePoint, number)
                    }
                }
                else {
                    when (mode) {
                        ConvertMode.ALL -> appendNumCharRef(codePoint, number)
                        ConvertMode.SKIP_BR -> {
                            if (!isBr(codePoint)) {
                                appendNumCharRef(codePoint, number)
                            }
                            else {
                                appendCodePoint(codePoint)
                            }
                        }
                        else -> appendCodePoint(codePoint)
                    }
                }

                index += 1
            }
        }
    }
}