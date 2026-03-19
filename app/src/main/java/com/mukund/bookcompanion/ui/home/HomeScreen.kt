package com.mukund.bookcompanion.ui.home

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.home.components.CustomBottomBar
import com.mukund.bookcompanion.ui.home.components.CustomHomeTopBar
import com.mukund.bookcompanion.ui.home.components.HomeContent

enum class BookCategory(val icon: Int) {
    All(R.drawable.list_alt),
    Read(R.drawable.check_circle),
    Unread(R.drawable.check_circle_unread);
}

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

    LaunchedEffect(currentCategory) {
        visibleStateAll.targetState    = currentCategory == BookCategory.All
        visibleStateRead.targetState   = currentCategory == BookCategory.Read
        visibleStateUnread.targetState = currentCategory == BookCategory.Unread
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            CustomHomeTopBar(
                settings = settings,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            CustomBottomBar(
                viewModel = viewModel,
                categories = BookCategory.entries.toTypedArray(),
                currentCategory = currentCategory,
                setCurrentCategory = { currentCategory = it },
                visibleStateAll = visibleStateAll,
                visibleStateRead = visibleStateRead,
                visibleStateUnread = visibleStateUnread,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            HomeContent(
                books = viewModel.books,
                navigateTo = navigateTo,
                currentCategory = currentCategory,
                visibleStateAll = visibleStateAll,
                visibleStateRead = visibleStateRead,
                visibleStateUnread = visibleStateUnread,
            )
        }
    }
}
