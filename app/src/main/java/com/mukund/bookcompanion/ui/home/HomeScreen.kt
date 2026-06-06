package com.mukund.bookcompanion.ui.home

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.ui.home.components.CustomBottomBar
import com.mukund.bookcompanion.ui.home.components.CustomHomeTopBarNew
import com.mukund.bookcompanion.ui.home.components.HomeContent
import com.mukund.bookcompanion.ui.theme.bookColors
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }

    var currentCategory by remember { mutableStateOf(BookCategory.All) }

    val visibleStateAll     = remember { MutableTransitionState(false) }
    val visibleStateRead    = remember { MutableTransitionState(false) }
    val visibleStateUnread  = remember { MutableTransitionState(false) }
    val visibleStateReading = remember { MutableTransitionState(false) }

    LaunchedEffect(currentCategory) {
        visibleStateAll.targetState     = currentCategory == BookCategory.All
        visibleStateRead.targetState    = currentCategory == BookCategory.Read
        visibleStateUnread.targetState  = currentCategory == BookCategory.Unread
        visibleStateReading.targetState = currentCategory == BookCategory.Reading
    }


    Scaffold(
        containerColor = bookColors.paper,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CustomHomeTopBarNew(
                settings = settings,
                categories = BookCategory.entries.toTypedArray(),
                currentCategory = currentCategory,
                setCurrentCategory = { currentCategory = it },
                visibleStateAll = visibleStateAll,
                visibleStateRead = visibleStateRead,
                visibleStateUnread = visibleStateUnread,
                books = viewModel.books
            )
        },
        bottomBar = {
            CustomBottomBar(
                viewModel = viewModel
            )
        },
    ) { paddingValues ->
        // No clip, no rounded container, no surfaceContainerHigh —
        // content bleeds edge-to-edge on the warm paper background
        HomeContent(
            books = viewModel.books,
            navigateTo = navigateTo,
            currentCategory = currentCategory,
            visibleStateAll = visibleStateAll,
            visibleStateRead = visibleStateRead,
            visibleStateUnread = visibleStateUnread,
            visibleStateReading = visibleStateReading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}