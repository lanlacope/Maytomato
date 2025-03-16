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

fun TextFieldValue.isSelectedPrefix(): Boolean {
    val breakPositions = text.indices.filter { i -> text[i] == '\n' }.map { it + 1 }
    return selection.start == 0 || selection.start in breakPositions
}

fun TextFieldValue.appendText(text: String): TextFieldValue {
    val newtext = buildAnnotatedString {
        append(this@appendText.text)
        append(text)
    }
    return this.copy(annotatedString = newtext)

}