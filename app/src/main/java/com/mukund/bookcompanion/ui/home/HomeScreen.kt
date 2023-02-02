package com.mukund.bookcompanion.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.ui.home.components.*
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit
) {
    var state by remember { mutableStateOf(0) }
    val books by viewModel.books.collectAsState(
        initial = emptyList()
    )
    BooksCompanionTheme() {
        Scaffold(
            topBar = {
                state = CustomHomeTopBar(settings)
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
                navigateTo = navigateTo,
                state = state
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
