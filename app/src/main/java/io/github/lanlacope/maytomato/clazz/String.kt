package io.github.lanlacope.maytomato.clazz

fun String.toDecimal(): String {
    return buildString {
        var index = 0
        while (index < this@toDecimal.length) {
            val codePoint = this@toDecimal.codePointAt(index)
            appendDecimal(codePoint)
            // サロゲートペアの場合は下位文字の分も進める
            index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
        }
    }
}

fun String.toHexadecimal(): String {
    return buildString {
        var index = 0
        while (index < this@toHexadecimal.length) {
            val codePoint = this@toHexadecimal.codePointAt(index)
            appendHexadecimal(codePoint)
            // サロゲートペアの場合は下位文字の分も進める
            index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
        }
    }
}

fun String.toEntity(): String {
    return buildString {
        var index = 0
        while (index < this@toEntity.length) {
            val codePoint = this@toEntity.codePointAt(index)
            appendEntity(codePoint)
            // サロゲートペアの場合は下位文字の分も進める
            index += if (Character.isSupplementaryCodePoint(codePoint)) 2 else 1
        }
    }
}