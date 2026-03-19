package com.mukund.bookcompanion.ui.overview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.overview.components.OverviewContent
import com.mukund.bookcompanion.ui.overview.components.OverviewTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(
    bookId: Int,
    backPress: () -> Boolean,
    navigateTo: () -> Unit,
    viewModel: BooksViewModel = hiltViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.getBook(bookId)
    }

    Scaffold(
        topBar = {
            OverviewTopBar(
                onBackPress = backPress,
                onNavigateTo = navigateTo
            )
        }
    ) { innerPadding ->
        OverviewContent(
            modifier = Modifier.padding(innerPadding),
            book = viewModel.book,
            onNavigateTo = navigateTo,
            onDeleteBook = { book: Book -> viewModel.deleteBook(book) },
            onBackPress = backPress
        )
    }
}
