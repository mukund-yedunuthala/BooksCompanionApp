package com.mukund.bookcompanion.ui.home.components

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
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomHomeTopBar(
    settings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) {
    val haptic = LocalHapticFeedback.current

    LargeFlexibleTopAppBar(
        title = {
            Text(
                text = "Book Companion",
            )
        },
        subtitle = {
            Text(text = "A minimal reading list tracker")
        },
        actions = {
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    settings()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "Settings",
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        scrollBehavior = scrollBehavior,
    )
}

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
                .padding(horizontal = 28.dp)
        ) {

            // ── Month/year micro bar ──────────────────────────────
            val monthYear = remember {
                java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("MMMM · yy"))
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
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        settings()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.settings),
                        contentDescription = "Settings",
                        tint = bookColors.inkFaint,
                        modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                    )
                }
            }

            // ── Wordmark ──────────────────────────────────────────
            Text(
                text = "Book",
                style = AppType.displaySerifItalic,
                color = bookColors.ink,
            )
            Text(
                text = "Companion",
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
                thickness = 0.5.dp,
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
                        text = "PROGRESS, ALL TIME",
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
                            text = "of",
                            style = AppType.headingSerifLight,
                            color = bookColors.inkFaint,
                        )
                        Text(
                            text = totalCount.toString(),
                            style = AppType.headingSerif,
                            color = bookColors.ink,
                        )
                        Text(
                            text = "read",
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
                thickness = 0.5.dp,
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
                    .padding(end = 20.dp, top = 4.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = category.name,
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