package com.mukund.bookcompanion.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_ID
import com.mukund.bookcompanion.navigation.Screen.*
import com.mukund.bookcompanion.ui.edit.EditScreen
import com.mukund.bookcompanion.ui.home.HomeScreen
import com.mukund.bookcompanion.ui.overview.Overview
import com.mukund.bookcompanion.ui.settings.SettingScreen
import com.mukund.bookcompanion.ui.settings.backup.Backup_Screen
import com.mukund.bookcompanion.ui.settings.LibsScreen
import com.mukund.bookcompanion.ui.settings.SettingsViewModel
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val darkTheme = viewModel.hasUserDarkThemeEnabled
    val systemTheme = viewModel.followSystemTheme
    val darkSetting = if (systemTheme) {
        isSystemInDarkTheme()
    }
    else {
        darkTheme
    }
    BooksCompanionTheme(darkTheme = darkSetting) {
        Surface {
            NavHost(
                navController = navController,
                startDestination = BooksScreen.route
            ) {
                // HOME
                composable(
                    route = BooksScreen.route,
                    enterTransition = customEnterTransition(),
                    popEnterTransition = customPopEnterTransition(),
                    exitTransition = customExitTransition()
                ) {
                    HomeScreen(
                        navigateTo = {
                            navController.navigate("${OverviewScreen.route}/${it}")
                        },
                        settings = {
                            navController.navigate(SettingsScreen.route)
                        }
                    )
                }
                // UPDATE
                composable(
                    route = "${UpdateBookScreen.route}/{$BOOK_ID}",
                    arguments = listOf(
                        navArgument(BOOK_ID) {
                            type = IntType
                        },
                    ),
                    enterTransition = customEnterTransition(),
                    popExitTransition = customPopExitTransition()
                ) { navBackStackEntry ->
                    val bookId = navBackStackEntry.arguments?.getInt(BOOK_ID) ?: 0
                    EditScreen(
                        bookId = bookId,
                        backPress = {
                            navController.popBackStack()
                        }
                    )
                }
                // SETTINGS
                composable(
                    route = SettingsScreen.route,
                    enterTransition = customEnterTransition(),
                    exitTransition = customExitTransition(),
                    popExitTransition = customPopExitTransition(),
                    popEnterTransition = customPopEnterTransition()
                ) {
                    SettingScreen(
                        backPress = { navController.popBackStack() },
                        libraries = { navController.navigate(LibrariesScreen.route) },
                        backup = { navController.navigate(BackupScreen.route) }
                    )
                }
                // OSS LIBS
                composable(
                    route = LibrariesScreen.route,
                    popExitTransition = customPopExitTransition(),
                    enterTransition = customEnterTransition()
                ) {
                    LibsScreen {
                        navController.popBackStack()
                    }
                }
                // OVERVIEW
                composable(
                    route = "${OverviewScreen.route}/{$BOOK_ID}",
                    arguments = listOf(
                        navArgument(BOOK_ID) {
                            type = IntType
                        },
                    ),
                    enterTransition = customEnterTransition(),
                    exitTransition = customExitTransition(),
                    popExitTransition = customPopExitTransition(),
                    popEnterTransition = customPopEnterTransition()
                ) { navBackStackEntry ->
                    val bookId = navBackStackEntry.arguments?.getInt(BOOK_ID) ?: 0
                    Overview(
                        bookId = bookId,
                        backPress = { navController.popBackStack() },
                        navigateTo = {
                            navController.navigate("${UpdateBookScreen.route}/${bookId}")
                        }
                    )
                }
                // BACKUP
                composable(
                    route = BackupScreen.route,
                    popExitTransition = customPopExitTransition(),
                    enterTransition = customEnterTransition()
                ) {
                    Backup_Screen { navController.popBackStack() }
                }
            }
        }
    }
}