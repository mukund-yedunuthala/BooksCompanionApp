package com.mukund.bookcompanion.ui.home

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.ui.home.components.*

enum class BookCategory(val icon: ImageVector) {
    All(Icons.Default.List),
    Read(Icons.Filled.CheckCircle),
    Unread(Icons.Outlined.CheckCircle);
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BooksViewModel = hiltViewModel(),
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }
    val onFabClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.openDialog()
    }
    val context = LocalContext.current

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


    val currentConfiguration = LocalConfiguration.current
    val screenOrientation = currentConfiguration.orientation
    if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLayout(viewModel, navigateTo, settings,
            haptic, context, onFabClick, visibilityState, currentCategory, setCurrentCategory,
            visibleStateAll, visibleStateRead, visibleStateUnread)
    }
    else {
        LandscapeLayout(viewModel, navigateTo, settings,
            haptic, context, onFabClick, visibilityState, currentCategory, setCurrentCategory,
            visibleStateAll, visibleStateRead, visibleStateUnread
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun PortraitLayout(
    viewModel: BooksViewModel,
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit,
    haptic: HapticFeedback,
    context: Context,
    onFabClick: () -> Unit,
    visibilityState: MutableTransitionState<Boolean>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
) {
    var sortedBy by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            CustomHomeTopBar(settings, haptic, onSortSelected = { selectedOption ->
                sortedBy = selectedOption
                // Call your sorting function here with the selectedOption
            },
                selectedIndex = selectedIndex,
                onRadioSelected = { index ->
                    selectedIndex = index
                }
            )
        },
        bottomBar = {
            CustomBottomBar(
                onFABClick = onFabClick,
                haptic,
                categories = BookCategory.values(),
                currentCategory,
                setCurrentCategory,
                visibleStateAll,
                visibleStateRead,
                visibleStateUnread
            )
        },
    ) { paddingValues ->
        AnimatedVisibility(
            visibleState = visibilityState,
            enter = fadeIn(
                initialAlpha = 0.0f,
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            HomeContent(
                paddingValues = paddingValues,
                books = viewModel.books,
                navigateTo = navigateTo,
                currentCategory,
                visibleStateAll, visibleStateRead, visibleStateUnread
            )
        }
        BookAdditionDialog(
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

@ExperimentalMaterial3Api
@Composable
fun LandscapeLayout(
    viewModel: BooksViewModel,
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit,
    haptic: HapticFeedback,
    context: Context,
    onFabClick: () -> Unit,
    visibilityState: MutableTransitionState<Boolean>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
) {
    Scaffold(
        topBar = {
            CustomLandscapeHomeTopBar(settings, haptic, context) // Move the top bar outside the Scaffold
        }
    ) { paddingValues ->
        Row(Modifier.fillMaxSize()) {
            CustomNavigationRail(
                currentCategory = currentCategory,
                setCurrentCategory = setCurrentCategory,
                onFabClick = onFabClick
            )
            AnimatedVisibility(
                visibleState = visibilityState,
                enter = fadeIn(
                    initialAlpha = 0.0f,
                    animationSpec = tween(durationMillis = 1000)
                )
            ) {
                HomeContent(
                    paddingValues = paddingValues,
                    books = viewModel.books,
                    navigateTo = navigateTo,
                    currentCategory,
                    visibleStateAll, visibleStateRead, visibleStateUnread
                )
            }
        }
        BookAdditionDialog(
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

