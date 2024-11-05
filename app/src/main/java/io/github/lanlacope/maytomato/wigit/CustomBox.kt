package io.github.lanlacope.maytomato.widgit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Suppress("unused")
@Composable
inline fun Box(
    noinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            ),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}

@Suppress("unused")
@OptIn(ExperimentalFoundationApi::class)
@Composable
inline fun Box(
    modifier: Modifier = Modifier,
    noinline onClick: (() -> Unit)? = null,
    noinline onLongClick: () -> Unit,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .combinedClickable(
                onClick = onClick?: { },
                onLongClick = onLongClick
            ),

        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}

@Suppress("unused")
@Composable
fun Box(
    onClick: () -> Unit,
    modifier: Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clickable (
                onClick = onClick
            )
        )
}

@Suppress("unused")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Box(
    onClick: (() -> Unit)? = null,
    onLongClick: () -> Unit,
    modifier: Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .combinedClickable(
                onClick = onClick?: { },
                onLongClick = onLongClick
            ),

    )
}