package com.mukund.bookcompanion.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.ui.home.components.*
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

enum class BookCategory(val icon: ImageVector) {
    All(Icons.Default.List),
    Read(Icons.Filled.CheckCircle),
    Unread(Icons.Outlined.CheckCircle);

    companion object {
        fun fromString(value: String): BookCategory {
            return values().find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid BookCategory value: $value")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }
    var state by remember { mutableStateOf(0) }

    val visibilityState = remember { MutableTransitionState(false) }
    visibilityState.targetState = true
    val (currentCategory, setCurrentCategory) = remember { mutableStateOf(BookCategory.All) }
    val updateKey = rememberUpdatedState(currentCategory)
    val visibleStateAll = remember { MutableTransitionState(false) }
    val visibleStateRead = remember { MutableTransitionState(false) }
    val visibleStateUnread = remember { MutableTransitionState(false) }
    LaunchedEffect(updateKey.value) {
        when (currentCategory) {
            BookCategory.All -> visibleStateAll.targetState = true
            BookCategory.Read -> visibleStateRead.targetState = true
            BookCategory.Unread -> visibleStateUnread.targetState = true
        }
    }
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    BooksCompanionTheme() {
        Scaffold(
            topBar = {
                CustomHomeTopBar(settings, haptic, context)
            },
            bottomBar = {
                CustomBottomBar(
                    onFABClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.openDialog()
                    },
                    haptic,
                    categories = BookCategory.values(),
                    currentCategory, setCurrentCategory, visibleStateAll, visibleStateRead, visibleStateUnread
                )
            },
        ) { paddingValues ->
            AnimatedVisibility(
                visibleState = visibilityState,
                enter = fadeIn(
                    initialAlpha = 0.0f,
                    animationSpec = tween(durationMillis = 1000)
                )
            )  {
                HomeContent(
                    paddingValues = paddingValues,
                    books = viewModel.books,
                    navigateTo = navigateTo,
                    currentCategory,
                    visibleStateAll, visibleStateRead, visibleStateUnread
                )
            }
            BookAdditionDialog(
                paddingValues = paddingValues,
                openDialog = viewModel.openDialog,
                closeDialog = {
                    viewModel.closeDialog()
                },
                addBook = { book ->
                    viewModel.addBook(book)
                },
                haptic,
                books = viewModel.books
            )
        }
    }
}
