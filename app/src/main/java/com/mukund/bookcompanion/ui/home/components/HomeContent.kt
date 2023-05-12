package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mukund.bookcompanion.domain.repository.Books
import com.mukund.bookcompanion.ui.home.BookCategory

@OptIn(ExperimentalFoundationApi::class)
@Composable
@ExperimentalMaterial3Api
fun HomeContent(
    paddingValues: PaddingValues,
    books: Books,
    navigateTo: (id: Int) -> Unit,
    currentCategory: BookCategory,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        itemsIndexed(
            items = books.filter {
                when(currentCategory) {
                    BookCategory.Read -> (it.status == "Read")
                    BookCategory.Unread -> (it.status == "Unread")
                    else -> {true}
                }
            }
        ) { index, book ->
            val modifier = Modifier.animateItemPlacement(
                animationSpec = tween(400)
            )
            CustomBookCard(
                book = book,
                navigateTo = navigateTo,
                modifier = modifier,
                index = index,
                visibleState = when (currentCategory) {
                    BookCategory.All -> visibleStateAll
                    BookCategory.Read -> visibleStateRead
                    BookCategory.Unread -> visibleStateUnread
                },
            )
        }
    }
}
