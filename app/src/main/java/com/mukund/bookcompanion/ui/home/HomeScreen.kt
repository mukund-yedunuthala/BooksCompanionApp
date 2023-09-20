package com.mukund.bookcompanion.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.ui.home.components.*

enum class BookCategory(val icon: ImageVector) {
    All(Icons.AutoMirrored.Filled.List),
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
    // viewModel
    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }
    // Haptic
    val haptic = LocalHapticFeedback.current
    // FabClick
    val onFabClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.openDialog()
    }
    // Bottom bar
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
    // Sorting dropdown
    var sortedBy = remember { mutableStateOf("") }
    var selectedIndex = remember { mutableStateOf(0) }
    val expandSortDropDown = remember { mutableStateOf(false) }
    // Orientation
    val currentConfiguration = LocalConfiguration.current
    val screenOrientation = currentConfiguration.orientation
    if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLayout(viewModel, navigateTo, settings,
            haptic, onFabClick, visibilityState, currentCategory, setCurrentCategory,
            visibleStateAll, visibleStateRead, visibleStateUnread,
            sortedBy, selectedIndex, expandSortDropDown
        )
    }
    else {
        LandscapeLayout(viewModel, navigateTo, settings,
            haptic, onFabClick, visibilityState, currentCategory, setCurrentCategory,
            visibleStateAll, visibleStateRead, visibleStateUnread,
            sortedBy, selectedIndex, expandSortDropDown
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
    onFabClick: () -> Unit,
    visibilityState: MutableTransitionState<Boolean>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
    sortedBy: MutableState<String>,
    selectedIndex: MutableState<Int>,
    expandSortDropDown: MutableState<Boolean>,
) {
    Scaffold(
        topBar = {
            CustomHomeTopBar(
                settings = settings,
                haptic = haptic,
                onSortSelected = { selectedOption ->
                    sortedBy.value = selectedOption
                    // Call your sorting function here with the selectedOption
                },
                sortingOptions = listOf("Title", "Year"),
                selectedIndex = selectedIndex.value,
                onRadioSelected = { index ->
                    selectedIndex.value = index
                },
                expandSortDropDown = expandSortDropDown
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
    onFabClick: () -> Unit,
    visibilityState: MutableTransitionState<Boolean>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
    sortedBy: MutableState<String>,
    selectedIndex: MutableState<Int>,
    expandSortDropDown: MutableState<Boolean>,
) {
    Scaffold(
        topBar = {
            CustomLandscapeHomeTopBar(
                settings = settings,
                haptic = haptic,
                onSortSelected = { selectedOption ->
                    sortedBy.value = selectedOption
                    // Call your sorting function here with the selectedOption
                },
                sortingOptions = listOf("Title", "Year"),
                selectedIndex = selectedIndex.value,
                onRadioSelected = { index ->
                    selectedIndex.value = index
                },
                expandSortDropDown = expandSortDropDown
            ) // Move the top bar outside the Scaffold
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

