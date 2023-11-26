package com.mukund.bookcompanion.ui.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.edit.components.UpdateContent
import com.mukund.bookcompanion.ui.home.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    bookId : Int,
    backPress : () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getBook(bookId)
    }
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Updating...")
                },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                        "Return to home"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        UpdateContent(
            paddingValues = paddingValues,
            book = viewModel.book,
            updateTitle = {title:String ->
                viewModel.updateTitle(title)
            },
            updateAuthor = {author:String ->
                viewModel.updateAuthor(author)
            },
            updateYear = {year:String ->
                viewModel.updateYear(year)

            },
            updateStatus = {status:String ->
                viewModel.updateStatus(status)
            },
            updateGenre = {genre: String ->
                viewModel.updateGenre(genre)
            },
            updateISBN = {isbn: String ->
                viewModel.updateISBN(isbn)
            },
            updateBook = {book:Book ->
                viewModel.updateBook(book)
            },
            backPress = backPress
        )
    }
}