package com.mukund.bookcompanion.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_ID
import com.mukund.bookcompanion.navigation.Screen.BooksScreen
import com.mukund.bookcompanion.navigation.Screen.UpdateBookScreen
import com.mukund.bookcompanion.navigation.Screen.SettingsScreen
import com.mukund.bookcompanion.navigation.Screen.LibrariesScreen
import com.mukund.bookcompanion.ui.edit.EditScreen
import com.mukund.bookcompanion.ui.home.HomeScreen
import com.mukund.bookcompanion.ui.settings.SettingScreen
import com.mukund.bookcompanion.ui.settings.components.LibsScreen
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    BooksCompanionTheme() {
        Surface {
            AnimatedNavHost(
                navController = navController,
                startDestination = BooksScreen.route
            ) {
                composable(
                    route = BooksScreen.route,
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeIn(animationSpec = tween(300))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeOut(animationSpec = tween(300))
                    }
                ) {
                    HomeScreen(
                        navigateTo = {
                            navController.navigate("${UpdateBookScreen.route}/${it}")
                        },
                        settings = {
                            navController.navigate(SettingsScreen.route)
                        }
                    )
                }
                composable(
                    route = "${UpdateBookScreen.route}/{$BOOK_ID}",
                    arguments = listOf(
                        navArgument(BOOK_ID) {
                            type = IntType
                        },
                    ),
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeIn(animationSpec = tween(300))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeOut(animationSpec = tween(300))
                    }
                ) { navBackStackEntry ->
                    val bookId = navBackStackEntry.arguments?.getInt(BOOK_ID) ?: 0
                    EditScreen(
                        bookId = bookId,
                        backPress = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    route = SettingsScreen.route,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeIn(animationSpec = tween(300))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeOut(animationSpec = tween(300))
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeOut(animationSpec = tween(300))
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeIn(animationSpec = tween(300))
                    }
                ) {
                    SettingScreen(
                        backPress = { navController.popBackStack() }
                    ) { navController.navigate(LibrariesScreen.route) }
                }
                composable(
                    route = LibrariesScreen.route,
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeOut(animationSpec = tween(300))
                    },
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 300 },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) +
                                fadeIn(animationSpec = tween(300))
                    }
                ) {
                    LibsScreen {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}