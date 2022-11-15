package com.mukund.bookscompanion.ui.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mukund.bookscompanion.domain.model.Book
import com.mukund.bookscompanion.domain.repository.Books

@Composable
@ExperimentalMaterial3Api
fun HomeContent(
    paddingValues: PaddingValues,
    books: Books,
    deleteBook: (book : Book) -> Unit,
    navigateTo: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(items = books) { book ->
            CustomBookCard(
                book = book,
                deleteBook = deleteBook,
                navigateTo = navigateTo
            )
        }
    }
}
