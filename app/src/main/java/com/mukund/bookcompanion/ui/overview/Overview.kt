package com.mukund.bookcompanion.ui.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.BookDetailState
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.overview.components.OverviewContent
import com.mukund.bookcompanion.ui.overview.components.OverviewTopBar
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(
    bookId: Int,
    backPress: () -> Boolean,
    viewModel: BooksViewModel = hiltViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.getBook(bookId)
    }

    Scaffold(
        containerColor = bookColors.paper,
        topBar = {
            OverviewTopBar(
                onBackPress = backPress,
            )
        }
    ) { innerPadding ->
        when (val state = viewModel.bookDetail) {
            BookDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = bookColors.ink)
                }
            }

            BookDetailState.NotFound -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.overview_not_found),
                        style = AppType.body,
                        color = bookColors.inkSoft,
                    )
                }
            }

            is BookDetailState.Loaded -> {
                OverviewContent(
                    modifier = Modifier.padding(innerPadding),
                    book = state.book,
                    onDeleteBook = { book: Book -> viewModel.deleteBook(book) },
                    onUpdateBook = { book: Book -> viewModel.updateBook(book) },
                    onBackPress = backPress,
                )
            }
        }
    }
}
