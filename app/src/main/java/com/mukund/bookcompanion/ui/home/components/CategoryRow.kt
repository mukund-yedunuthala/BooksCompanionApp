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
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CategoryButtonGroup(
    currentCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val categories = listOf("Unread", "Read")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Status:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 10.dp)
        )

        ButtonGroup(
            modifier = Modifier.weight(1f),
            overflowIndicator = {}
        ) {
            categories.forEach { category ->
                toggleableItem(
                    checked = currentCategory == category,
                    onCheckedChange = { checked ->
                        if (checked) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            onCategorySelect(category)
                        }
                    },
                    weight = 1f,
                    label = category,
                )
            }
        }
    }
}

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
                        fontFamily = IBMPlexSans,
                        fontSize = 12.sp,
                        letterSpacing = 0.03.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isActive) bookColors.paper
                        else bookColors.inkSoft,
                    )
                }
            }
        }
    }
}