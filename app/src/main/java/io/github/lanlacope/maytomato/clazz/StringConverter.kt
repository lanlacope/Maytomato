package io.github.lanlacope.maytomato.clazz

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.persistentMapOf

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

object ConvertOption {
    const val ENTITY = "Entity"
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

    /*
     * コードポイントを、可能なら数値文字参照に変換してappend
     */
    private fun StringBuilder.appendNumCharRef(
        codePoint: Int,
        num: Int
    ) {
        when (num) {
            10 -> this.append("&#").append(codePoint.toString(10)).append(";")
            16 -> this.append("&#x").append(codePoint.toString(16)).append(";")
            else -> {
                Log.w("StringBuilder", "$num is not supported number!")
                this.appendCodePoint(codePoint)
            }
        }
    }

    /*
     * 実体参照の対応表
     */
    private val htmlEntityMap = persistentMapOf(
        34 to "quot", 38 to "amp", 39 to "apos", 60 to "lt", 62 to "gt",
        160 to "nbsp", 161 to "iexcl", 162 to "cent", 163 to "pound", 164 to "curren",
        165 to "yen", 166 to "brvbar", 167 to "sect", 168 to "uml", 169 to "copy",
        170 to "ordf", 171 to "laquo", 172 to "not", 173 to "shy", 174 to "reg",
        175 to "macr", 176 to "deg", 177 to "plus", 178 to "sup2", 179 to "sup3",
        180 to "acute", 181 to "micro", 182 to "para", 183 to "middot", 184 to "cedil",
        185 to "sup1", 186 to "ordm", 187 to "raquo", 188 to "frac14", 189 to "frac12",
        190 to "frac34", 191 to "iquest", 192 to "Agrave", 193 to "Aacute", 194 to "Acirc",
        195 to "Atilde", 196 to "Auml", 197 to "Aring", 198 to "AElig", 199 to "Ccedil",
        200 to "Egrave", 201 to "Eacute", 202 to "Ecirc", 203 to "Euml", 204 to "Igrave",
        205 to "Iacute", 206 to "Icirc", 207 to "Iuml", 208 to "ETH", 209 to "Ntilde",
        210 to "Ograve", 211 to "Oacute", 212 to "Ocirc", 213 to "Otilde", 214 to "Ouml",
        215 to "times", 216 to "Oslash", 217 to "Ugrave", 218 to "Uacute", 219 to "Ucirc",
        220 to "Uuml", 221 to "Yacute", 222 to "THORN", 223 to "szlig", 224 to "agrave",
        225 to "aacute", 226 to "acirc", 227 to "atilde", 228 to "auml", 229 to "aring",
        230 to "aelig", 231 to "ccedil", 232 to "egrave", 233 to "eacute", 234 to "ecirc",
        235 to "euml", 236 to "igrave", 237 to "iacute", 238 to "icirc", 239 to "iuml",
        240 to "eth", 241 to "ntilde", 242 to "ograve", 243 to "oacute", 244 to "ocirc",
        245 to "otilde", 246 to "ouml", 247 to "divide", 248 to "oslash", 249 to "ugrave",
        250 to "uacute", 251 to "ucirc", 252 to "uuml", 253 to "yacute", 254 to "thorn",
        255 to "yuml",
        338 to "OElig", 339 to "oelig", 352 to "Scaron", 353 to "scaron",
        376 to "Yuml", 402 to "fnof", 710 to "circ", 732 to "tilde",
        913 to "Alpha", 914 to "Beta", 915 to "Gamma", 916 to "Delta", 917 to "Epsilon",
        918 to "Zeta", 919 to "Eta", 920 to "Theta", 921 to "Iota", 922 to "Kappa",
        923 to "Lambda", 924 to "Mu", 925 to "Nu", 926 to "Xi", 927 to "Omicron",
        928 to "Pi", 929 to "Rho", 931 to "Sigma", 932 to "Tau", 933 to "Upsilon",
        934 to "Phi", 935 to "Chi", 936 to "Psi", 937 to "Omega",
        945 to "alpha", 946 to "beta", 947 to "gamma", 948 to "delta", 949 to "epsilon",
        950 to "zeta", 951 to "eta", 952 to "theta", 953 to "iota", 954 to "kappa",
        955 to "lambda", 956 to "mu", 957 to "nu", 958 to "xi", 959 to "omicron",
        960 to "pi", 961 to "rho", 962 to "sigmaf", 963 to "sigma", 964 to "tau",
        965 to "upsilon", 966 to "phi", 967 to "chi", 968 to "psi", 969 to "omega",
        977 to "thetasym", 978 to "upsih", 982 to "piv",
        8194 to "ensp", 8195 to "emsp", 8201 to "thinsp", 8204 to "zwnj", 8205 to "zwj",
        8206 to "lrm", 8207 to "rlm", 8211 to "ndash", 8212 to "mdash",
        8216 to "lsquo", 8217 to "rsquo", 8218 to "sbquo", 8220 to "ldquo", 8221 to "rdquo",
        8222 to "bdquo", 8224 to "dagger", 8225 to "Dagger", 8226 to "bull",
        8230 to "hellip", 8240 to "permil", 8242 to "prime", 8243 to "Prime",
        8249 to "lsaquo", 8250 to "rsaquo", 8254 to "oline", 8260 to "frasl", 8364 to "euro",
        8465 to "image", 8472 to "weierp", 8476 to "real", 8482 to "trade", 8501 to "alefsym",
        8592 to "larr", 8593 to "uarr", 8594 to "rarr", 8595 to "darr", 8596 to "harr",
        8629 to "crarr", 8656 to "lArr", 8657 to "uArr", 8658 to "rArr", 8659 to "dArr",
        8660 to "hArr",
        8704 to "forall", 8706 to "part", 8707 to "exist", 8709 to "empty", 8711 to "nabla",
        8712 to "isin", 8713 to "notin", 8715 to "ni", 8719 to "prod", 8721 to "sum",
        8722 to "minus", 8727 to "lowast", 8730 to "radic", 8733 to "prop", 8734 to "infin",
        8736 to "ang", 8743 to "and", 8744 to "or", 8745 to "cap", 8746 to "cup",
        8747 to "int", 8756 to "there4", 8773 to "cong", 8776 to "asymp", 8800 to "ne",
        8801 to "equiv", 8004 to "le", 8005 to "ge", 8834 to "sub", 8835 to "sup",
        8836 to "nsub", 8838 to "sube", 8839 to "supe", 8853 to "opulse", 8855 to "otimes",
        8869 to "prep", 8901 to "sdot", 8968 to "lceil", 8969 to "rceil",
        8970 to "lfloor", 8971 to "rfloor", 9001 to "lang", 9002 to "rang",
        9674 to "loz", 9824 to "spades", 9827 to "clubs", 9829 to "hearts", 9830 to "diams"
    )

    /*
     * 実体参照可能か判定
     */
    private fun isEntity(codePoint: Int): Boolean {
        return !htmlEntityMap[codePoint].isNullOrEmpty()
    }

    /*
     * コードポイントを、可能なら実態文字参照に変換してappend
     */
    private fun StringBuilder.appendEntityCharRef(
        codePoint: Int
    ) {
        this.append("&").append(htmlEntityMap[codePoint]).append(";")
    }


    fun startConvert(
        rawText: String,
        mode: String = ConvertMode.MOJIBAKE,
        number: Int = ConvertNumber.DEC,
        options: List<String> = emptyList()
    ): String {
        return buildString {
            var index = 0
            while (index < rawText.length) {
                val codePoint = rawText.codePointAt(index)

                if (ConvertOption.ENTITY in options && isEntity(codePoint)) {
                    appendEntityCharRef(codePoint)
                }
                else {
                    when (mode) {
                        ConvertMode.ALL -> {
                            appendNumCharRef(codePoint, number)
                        }

                        ConvertMode.SKIP_BR -> {
                            if (!isBreak(codePoint)) {
                                appendNumCharRef(codePoint, number)
                            } else {
                                appendCodePoint(codePoint)
                            }
                        }

                        ConvertMode.MOJIBAKE -> {
                            if (isZeroWidth(codePoint)) {
                                appendNumCharRef(codePoint, number)
                            } else {
                                appendCodePoint(codePoint)
                            }
                        }

                        ConvertMode.REMOVE -> {
                            if (!isZeroWidth(codePoint)) {
                                appendCodePoint(codePoint)
                            }
                        }
                    }
                }

                // サロゲートペアの場合は下位文字の分も進める
                index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
            }
        }
    }
}