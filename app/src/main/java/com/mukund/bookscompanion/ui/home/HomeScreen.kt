package com.mukund.bookscompanion.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookscompanion.ui.home.components.BookAdditionDialog
import com.mukund.bookscompanion.ui.home.components.CustomHomeFab
import com.mukund.bookscompanion.ui.home.components.CustomHomeTopBar
import com.mukund.bookscompanion.ui.home.components.HomeContent
import com.mukund.bookscompanion.ui.theme.BooksCompanionTheme

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    navigateTo: (id: Int) -> Unit
) {
    val books by viewModel.books.collectAsState(
        initial = emptyList()
    )
    BooksCompanionTheme() {


        Scaffold(
            topBar = {
                CustomHomeTopBar()
            },
            floatingActionButton = {
                CustomHomeFab(
                    onClick = {
                        viewModel.openDialog()
                    }
                )
            },
        ) { paddingValues ->
            HomeContent(
                paddingValues = paddingValues,
                books = books,
                deleteBook = { book ->
                    viewModel.deleteBook(book)
                },
                navigateTo = navigateTo
            )
            BookAdditionDialog(
                paddingValues = paddingValues,
                openDialog = viewModel.openDialog,
                closeDialog = {
                    viewModel.closeDialog()
                },
                addBook = { book ->
                    viewModel.addBook(book)
                },
            )
        }
    }
}
