package com.mukund.bookcompanion.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
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

    // Orientation
    val currentConfiguration = LocalConfiguration.current
//    val screenOrientation = currentConfiguration.orientation
    val screenOrientation = Configuration.ORIENTATION_PORTRAIT
    if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLayout(viewModel, navigateTo, settings,
            haptic, visibilityState, currentCategory, setCurrentCategory,
            visibleStateAll, visibleStateRead, visibleStateUnread,
        )
    }
    else {
//        LandscapeLayout(viewModel, navigateTo, settings,
//            haptic, onFabClick, visibilityState, currentCategory, setCurrentCategory,
//            visibleStateAll, visibleStateRead, visibleStateUnread,
//            sortedBy, selectedIndex, expandSortDropDown
//        )
    }


}

@ExperimentalMaterial3Api
@Composable
fun PortraitLayout(
    viewModel: BooksViewModel,
    navigateTo: (id: Int) -> Unit,
    settings: () -> Unit,
    haptic: HapticFeedback,
    visibilityState: MutableTransitionState<Boolean>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
) {
    Scaffold(
        topBar = {
            CustomHomeTopBar(
                settings = settings,
                haptic = haptic,
            )
        },
        bottomBar = {
            CustomBottomBar(
                viewModel = viewModel,
                haptic,
                categories = BookCategory.entries.toTypedArray(),
                currentCategory,
                setCurrentCategory,
                visibleStateAll,
                visibleStateRead,
                visibleStateUnread,
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
    }
}
//
//@ExperimentalMaterial3Api
//@Composable
//fun LandscapeLayout(
//    viewModel: BooksViewModel,
//    navigateTo: (id: Int) -> Unit,
//    settings: () -> Unit,
//    haptic: HapticFeedback,
//    onFabClick: () -> Unit,
//    visibilityState: MutableTransitionState<Boolean>,
//    currentCategory: BookCategory,
//    setCurrentCategory: (BookCategory) -> Unit,
//    visibleStateAll: MutableTransitionState<Boolean>,
//    visibleStateRead: MutableTransitionState<Boolean>,
//    visibleStateUnread: MutableTransitionState<Boolean>,
//    sortedBy: MutableState<String>,
//    selectedIndex: MutableState<Int>,
//    expandSortDropDown: MutableState<Boolean>,
//) {
//    Scaffold(
//        topBar = {
//            CustomLandscapeHomeTopBar(
//                settings = settings,
//                haptic = haptic,
//                onSortSelected = { selectedOption ->
//                    sortedBy.value = selectedOption
//                    // Call your sorting function here with the selectedOption
//                },
//                sortingOptions = listOf("Title", "Year"),
//                selectedIndex = selectedIndex.value,
//                onRadioSelected = { index ->
//                    selectedIndex.value = index
//                },
//                expandSortDropDown = expandSortDropDown
//            ) // Move the top bar outside the Scaffold
//        }
//    ) { paddingValues ->
//        Row(Modifier.fillMaxSize()) {
//            CustomNavigationRail(
//                currentCategory = currentCategory,
//                setCurrentCategory = setCurrentCategory,
//                onFabClick = onFabClick
//            )
//            AnimatedVisibility(
//                visibleState = visibilityState,
//                enter = fadeIn(
//                    initialAlpha = 0.0f,
//                    animationSpec = tween(durationMillis = 1000)
//                )
//            ) {
//                HomeContent(
//                    paddingValues = paddingValues,
//                    books = viewModel.books,
//                    navigateTo = navigateTo,
//                    currentCategory,
//                    visibleStateAll, visibleStateRead, visibleStateUnread
//                )
//            }
//        }
//    }
//}

