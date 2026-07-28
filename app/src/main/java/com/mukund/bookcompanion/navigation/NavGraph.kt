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
import com.mukund.bookcompanion.core.Constants.Companion.BACKUP_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_ID
import com.mukund.bookcompanion.core.Constants.Companion.BOOKS_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.LIBRARIES_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.OVERVIEW_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.SETTINGS_SCREEN
import com.mukund.bookcompanion.ui.home.HomeScreen
import com.mukund.bookcompanion.ui.overview.Overview
import com.mukund.bookcompanion.ui.settings.SettingScreen
import com.mukund.bookcompanion.ui.settings.backup.Backup_Screen
import com.mukund.bookcompanion.ui.settings.LibsScreen
import com.mukund.bookcompanion.ui.settings.SettingsViewModel
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

/**
 * Pure function extracted from NavGraph so it can be tested without Compose.
 * Returns true when dark mode should be active.
 */
fun resolveTheme(followSystem: Boolean, userDark: Boolean, isSystemDark: Boolean): Boolean =
    if (followSystem) isSystemDark else userDark

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val darkTheme = viewModel.hasUserDarkThemeEnabled
    val systemTheme = viewModel.followSystemTheme
    val darkSetting = resolveTheme(systemTheme, darkTheme, isSystemInDarkTheme())
    BooksCompanionTheme(darkTheme = darkSetting) {
        Surface {
            NavHost(
                navController = navController,
                startDestination = BOOKS_SCREEN
            ) {
                // HOME
                composable(
                    route = BOOKS_SCREEN,
                    enterTransition = customEnterTransition(),
                    popEnterTransition = customEnterTransition(),
                    exitTransition = customExitTransition()
                ) {
                    HomeScreen(
                        navigateTo = {
                            navController.navigate("$OVERVIEW_SCREEN/${it}")
                        },
                        settings = {
                            navController.navigate(SETTINGS_SCREEN)
                        }
                    )
                }
                // SETTINGS
                composable(
                    route = SETTINGS_SCREEN,
                    enterTransition = customEnterTransition(),
                    exitTransition = customExitTransition(),
                    popExitTransition = customExitTransition(),
                    popEnterTransition = customEnterTransition()
                ) {
                    SettingScreen(
                        backPress = { navController.popBackStack() },
                        libraries = { navController.navigate(LIBRARIES_SCREEN) },
                        backup = { navController.navigate(BACKUP_SCREEN) }
                    )
                }
                // OSS LIBS
                composable(
                    route = LIBRARIES_SCREEN,
                    popExitTransition = customExitTransition(),
                    enterTransition = customEnterTransition()
                ) {
                    LibsScreen {
                        navController.popBackStack()
                    }
                }
                // OVERVIEW
                composable(
                    route = "$OVERVIEW_SCREEN/{$BOOK_ID}",
                    arguments = listOf(
                        navArgument(BOOK_ID) {
                            type = IntType
                        },
                    ),
                    enterTransition = customEnterTransition(),
                    exitTransition = customExitTransition(),
                    popExitTransition = customExitTransition(),
                    popEnterTransition = customEnterTransition()
                ) { navBackStackEntry ->
                    val bookId = navBackStackEntry.arguments?.getInt(BOOK_ID) ?: 0
                    Overview(
                        bookId = bookId,
                        backPress = { navController.popBackStack() },
                    )
                }
                // BACKUP
                composable(
                    route = BACKUP_SCREEN,
                    popExitTransition = customExitTransition(),
                    enterTransition = customEnterTransition()
                ) {
                    Backup_Screen { navController.popBackStack() }
                }
            }
        }
    }
}
