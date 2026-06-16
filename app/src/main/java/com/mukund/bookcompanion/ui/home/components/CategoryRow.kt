package com.mukund.bookcompanion.ui.home.components

import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@Composable
fun CategoryButtonGroupNew(
    currentCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val categories = listOf(BookCategory.Unread, BookCategory.Reading, BookCategory.Read)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isActive = currentCategory == category.statusLabel

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = BookCompanionBorders.hairline,
                        color = if (isActive) bookColors.ink else bookColors.rule,
                    )
                    .background(
                        color = if (isActive) bookColors.ink
                        else Color.Transparent
                    )
                    .clickable(role = Role.RadioButton) {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        onCategorySelect(category.statusLabel.orEmpty())
                    }
                    .heightIn(min = 48.dp)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Status dot — matches the design's StatusDot
                    val dotColor = when (category) {
                        BookCategory.Read -> bookColors.sage
                        BookCategory.Reading -> bookColors.ochre
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .size(BookCompanionSpacing.statusDot)
                            .border(
                                width = 1.dp,
                                color = if (category == BookCategory.Unread)
                                    if (isActive) bookColors.paper.copy(alpha = 0.6f)
                                    else bookColors.inkFaint
                                else Color.Transparent,
                                shape = CircleShape
                            )
                            .background(dotColor, CircleShape)
                    )
                    Text(
                        text = stringResource(category.labelRes),
                        style = AppType.labelSmallLight,
                        color = if (isActive) bookColors.paper else bookColors.inkSoft,
                    )
                }
            }
        }
    }
}
