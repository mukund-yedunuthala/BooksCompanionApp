package com.mukund.bookcompanion.ui.home.components

import com.mukund.bookcompanion.design.BookCompanionSpacing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
@ExperimentalMaterial3Api
fun HomeContent(
    books: List<Book>,
    navigateTo: (id: Int) -> Unit,
    currentCategory: BookCategory,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
    visibleStateReading: MutableTransitionState<Boolean>,
    modifier: Modifier,
) {
    val filtered = remember(books, currentCategory) {
        books.filter {
            when (currentCategory.statusLabel) {
                null -> true
                else -> it.status == currentCategory.statusLabel
            }
        }
    }

    if (filtered.isEmpty()) {
        EmptyState(currentCategory = currentCategory, modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = filtered,
            key = { _, book -> book.id }
        ) { index, book ->
            val itemModifier = Modifier.animateItem(
                fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(400)
            )
            CustomBookCardNew(
                book = book,
                navigateTo = navigateTo,
                modifier = itemModifier,
                index = index,
                visibleState = when (currentCategory) {
                    BookCategory.All -> visibleStateAll
                    BookCategory.Read -> visibleStateRead
                    BookCategory.Unread -> visibleStateUnread
                    BookCategory.Reading -> visibleStateReading
                },
            )
        }
    }
}

@Composable
private fun EmptyState(
    currentCategory: BookCategory,
    modifier: Modifier = Modifier,
) {
    val message = when (val status = currentCategory.statusLabel) {
        null -> stringResource(R.string.home_empty_all)
        else -> stringResource(R.string.home_empty_filtered, status.lowercase())
    }
    Box(
        modifier = modifier.padding(horizontal = BookCompanionSpacing.gutter),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = AppType.body,
            color = bookColors.inkFaint,
            textAlign = TextAlign.Center,
        )
    }
}
