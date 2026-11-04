package com.mukund.bookcompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.design.BookCompanionSpacing
import com.mukund.bookcompanion.ui.theme.bookColors

/** The design's StatusDot: filled for Read/Reading, hollow ring when Unread.
 * Shared by the book card, the overview badge and the category filter row. */
@Composable
fun StatusDot(
    status: String,
    modifier: Modifier = Modifier,
    size: Dp = BookCompanionSpacing.statusDot,
    unreadRingColor: Color = bookColors.inkFaint,
) {
    val dotColor = statusColor(status)
    val isUnread = status.lowercase() == "unread"

    Box(
        modifier = modifier
            .size(size)
            .border(
                width = 1.dp,
                color = if (isUnread) unreadRingColor else Color.Transparent,
                shape = CircleShape
            )
            .background(dotColor, CircleShape)
    )
}

/** One home for the status→color mapping (sage = Read, ochre = Reading). */
@Composable
fun statusColor(status: String): Color = when (status.lowercase()) {
    "read"    -> bookColors.sage
    "reading" -> bookColors.ochre
    else      -> Color.Transparent
}
