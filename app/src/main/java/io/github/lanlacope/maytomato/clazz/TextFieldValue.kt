package io.github.lanlacope.maytomato.clazz

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import io.github.lanlacope.rewheel.util.getTextAfterSelection
import io.github.lanlacope.rewheel.util.getTextBeforeSelection

fun TextFieldValue.convertDecimalSelectedText(): TextFieldValue {
    val convertedText = getSelectedText().text.toDecimal()
    val newText = buildAnnotatedString {
        append(getTextBeforeSelection())
        append(convertedText)
        append(getTextAfterSelection())
    }
    val newCursor = getTextBeforeSelection().length + convertedText.length
    return this.copy(annotatedString = newText, selection = TextRange(newCursor, newCursor))
}

fun TextFieldValue.convertHexadecimalSelectedText(): TextFieldValue {
    val convertedText = getSelectedText().text.toHexadecimal()
    val newText = buildAnnotatedString {
        append(getTextBeforeSelection())
        append(convertedText)
        append(getTextAfterSelection())
    }
    val newCursor = getTextBeforeSelection().length + convertedText.length
    return this.copy(annotatedString = newText, selection = TextRange(newCursor, newCursor))
}

fun TextFieldValue.convertEntitySelectedText(): TextFieldValue {
    val convertedText = getSelectedText().text.toEntity()
    val newText = buildAnnotatedString {
        append(getTextBeforeSelection())
        append(convertedText)
        append(getTextAfterSelection())
    }
    val newCursor = getTextBeforeSelection().length + convertedText.length
    return this.copy(annotatedString = newText, selection = TextRange(newCursor, newCursor))
}