package io.github.lanlacope.maytomato.activity.component.text

import android.util.Patterns
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.TextFieldValue
import io.github.lanlacope.rewheel.ui.text.selectable.SelectableText

@Composable
fun ResultText(
    value: String,
    modifier: Modifier = Modifier
) {

    val uriHandler = LocalUriHandler.current

    val annotatedValue = AnnotatedString.fromHtml(
        htmlString = value,
        linkStyles = TextLinkStyles(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary
            )
        ),
        linkInteractionListener = { annotation ->
            val url = (annotation as? LinkAnnotation.Url)?.url
            if (url != null) uriHandler.openUri(url)
        }
    )
    var textFieldValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                annotatedString = annotatedValue
            )
        )
    }

    ResultTextToolbar(
        value = textFieldValue,
        onValueChange = { textFieldValue = it }
    ) {
        SelectableText(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            modifier = modifier
        )
    }
}