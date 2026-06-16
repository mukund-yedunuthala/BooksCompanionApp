package com.mukund.bookcompanion.ui.home.components

import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.ui.home.SortOption
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomHomeTopBarNew(
    settings: () -> Unit,
    categories: Array<BookCategory>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
    visibleStateReading: MutableTransitionState<Boolean>,
    books: List<Book>,
    sortOption: SortOption,
    onSortClick: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        color = bookColors.paper,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = BookCompanionSpacing.gutter)
        ) {

            // ── Month/year micro bar ──────────────────────────────
            val monthYearPattern = stringResource(R.string.home_month_year_pattern)
            val monthYear = remember(monthYearPattern) {
                java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern(monthYearPattern))
                    .uppercase()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthYear,
                    style = AppType.labelTinyMono,
                    color = bookColors.inkFaint,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val nextSortOption = SortOption.entries[(sortOption.ordinal + 1) % SortOption.entries.size]
                    Text(
                        text = stringResource(sortOption.labelRes).uppercase(),
                        style = AppType.labelTinyMono,
                        color = bookColors.inkFaint,
                    )
                    // No explicit size() — IconButton keeps its 48dp minimum touch target;
                    // only the inner Icon glyph is sized down.
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            onSortClick()
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sort),
                            contentDescription = stringResource(
                                R.string.home_sort_by, stringResource(nextSortOption.labelRes)
                            ),
                            tint = bookColors.inkFaint,
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                        )
                    }
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            settings()
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = stringResource(R.string.settingsscreen_toplabel),
                            tint = bookColors.inkFaint,
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                        )
                    }
                }
            }

            // ── Wordmark ──────────────────────────────────────────
            Text(
                text = stringResource(R.string.home_wordmark_line1),
                style = AppType.displaySerifItalic,
                color = bookColors.ink,
            )
            Text(
                text = stringResource(R.string.home_wordmark_line2),
                style = AppType.displaySerif,
                color = bookColors.ink,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // ── Progress tracker placeholder ──────────────────────
            // TODO: Replace with real reading progress stat
            //       showing "X of Y read" + mini bar chart
            //       mirroring the React Header progress section.
            //       Data should come from viewModel.books.
            HorizontalDivider(
                color = bookColors.rule,
                thickness = BookCompanionBorders.hairline,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                val readCount = remember(books) {
                    books.count { it.status == BookCategory.Read.statusLabel }
                }
                val totalCount = books.size

                Column {
                    Text(
                        text = stringResource(R.string.home_progress_label).uppercase(),
                        style = AppType.labelMicroMono,
                        color = bookColors.inkFaint,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = readCount.toString(),
                            style = AppType.headingSerif,
                            color = bookColors.ink,
                        )
                        Text(
                            text = stringResource(R.string.home_progress_of),
                            style = AppType.headingSerifLight,
                            color = bookColors.inkFaint,
                        )
                        Text(
                            text = totalCount.toString(),
                            style = AppType.headingSerif,
                            color = bookColors.ink,
                        )
                        Text(
                            text = stringResource(R.string.home_progress_read),
                            style = AppType.bodySmall,
                            color = bookColors.inkFaint,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // ── Hairline divider ──────────────────────────────────
            HorizontalDivider(
                color = bookColors.rule,
                thickness = BookCompanionBorders.hairline,
            )

            // ── Filter tabs ───────────────────────────────────────
            FilterTabs(
                categories = categories,
                currentCategory = currentCategory,
                setCurrentCategory = { selected ->
                    setCurrentCategory(selected)
                    visibleStateAll.targetState = false
                    visibleStateRead.targetState = false
                    visibleStateUnread.targetState = false
                    visibleStateReading.targetState = false
                },
                modifier = Modifier.padding(vertical = 4.dp),
                books = books
            )
        }
    }
}

// ── Filter tabs ───────────────────────────────────────────────────────────────
// Extracted from old bottom bar ToggleButton group.
// Reusable — can be called from top bar, or anywhere else that needs filtering.
@Composable
fun FilterTabs(
    categories: Array<BookCategory>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    books: List<Book>,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        categories.forEach { category ->
            val isActive = currentCategory == category
            val count = remember(books, category) {
                when (category.statusLabel) {
                    null -> books.size
                    else -> books.count { it.status == category.statusLabel }
                }
            }

            Column(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (!isActive) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            setCurrentCategory(category)
                        }
                    }
                    .heightIn(min = 48.dp)
                    .padding(end = 20.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(category.labelRes),
                        style = if (isActive) AppType.labelSmall else AppType.labelSmallLight,
                        color = if (isActive) bookColors.ink else bookColors.inkFaint,
                    )
                    Text(
                        text = count.toString().padStart(2, '0'),
                        style = AppType.labelMicroMono,
                        color = if (isActive) bookColors.terracotta else bookColors.inkFaint,
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .wrapContentWidth()
                        .height(1.5.dp)
                        .background(
                            color = if (isActive) bookColors.ink
                            else Color.Transparent
                        )
                )
            }
        }
    }
}