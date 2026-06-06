package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@Composable
fun CategoryButtonGroupNew(
    currentCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val categories = listOf("Unread", "Reading", "Read")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isActive = currentCategory == category

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 0.5.dp,
                        color = if (isActive) bookColors.ink else bookColors.rule,
                    )
                    .background(
                        color = if (isActive) bookColors.ink
                        else Color.Transparent
                    )
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        onCategorySelect(category)
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Status dot — matches the design's StatusDot
                    val dotColor = when (category) {
                        "Read"    -> bookColors.sage
                        "Reading" -> bookColors.ochre
                        else      -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .border(
                                width = 1.dp,
                                color = if (category == "Unread")
                                    if (isActive) bookColors.paper.copy(alpha = 0.6f)
                                    else bookColors.inkFaint
                                else Color.Transparent,
                                shape = CircleShape
                            )
                            .background(dotColor, CircleShape)
                    )
                    Text(
                        text = category,
                        style = AppType.labelSmallLight,
                        color = if (isActive) bookColors.paper else bookColors.inkSoft,
                    )
                }
            }
        }
    }
}