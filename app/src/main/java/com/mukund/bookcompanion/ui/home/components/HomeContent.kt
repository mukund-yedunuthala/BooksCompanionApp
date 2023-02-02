package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.Books

@Composable
@ExperimentalMaterial3Api
fun HomeContent(
    paddingValues: PaddingValues,
    books: Books,
    deleteBook: (book: Book) -> Unit,
    navigateTo: (id: Int) -> Unit,
    state: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(items = books) { book ->
            when (state) {
                0 -> {
                    CustomBookCard(
                        book = book,
                        deleteBook = deleteBook,
                        navigateTo = navigateTo
                    )
                }
                1 -> {
                    if (book.status == "Read") {
                        CustomBookCard(
                            book = book,
                            deleteBook = deleteBook,
                            navigateTo = navigateTo
                        )
                    }
                }
                2 -> {
                    if (book.status == "Unread") {
                        CustomBookCard(
                            book = book,
                            deleteBook = deleteBook,
                            navigateTo = navigateTo
                        )
                    }
                }
            }
        }
    }
}
