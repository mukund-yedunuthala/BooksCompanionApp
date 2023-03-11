package com.mukund.bookcompanion.ui.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.overview.components.OverviewContent
import com.mukund.bookcompanion.ui.overview.components.OverviewTopBar

@Composable
fun Overview(
    viewModel: BooksViewModel = hiltViewModel(),
    bookId: Int,
    backPress: () -> Boolean,
    navigateTo: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getBook(bookId)
    }
    Scaffold(
        topBar = {
            OverviewTopBar(backPress, navigateTo)
        },
    ) {
        Surface(modifier = Modifier.padding(it)) {
            OverviewContent(
                viewModel.book,
                navigateTo,
                deleteBook = { book: Book ->
                    viewModel.deleteBook(book)
                },
                backPress
            )
        }
    }
}

