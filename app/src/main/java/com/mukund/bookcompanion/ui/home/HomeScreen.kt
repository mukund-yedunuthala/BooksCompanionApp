package com.mukund.bookcompanion.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    BooksCompanionTheme() {
        Scaffold(
            topBar = {
                //state = CustomHomeTopBar(settings)
                CustomHomeTopBar(settings, haptic, context)
            },
            /*floatingActionButton = {
                CustomHomeFab(
                    onClick = {
                        viewModel.openDialog()
                    }
                )
            },*/
            bottomBar = {
                state = CustomBottomBar(
                    onFABClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.openDialog()
                    },
                    haptic
                )
            }
        ) { paddingValues ->
            HomeContent(
                paddingValues = paddingValues,
                books = books,
                deleteBook = { book ->
                    viewModel.deleteBook(book)
                },
                navigateTo = navigateTo,
                state = state,
                haptic
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
                haptic
            )
        }
    }
}
