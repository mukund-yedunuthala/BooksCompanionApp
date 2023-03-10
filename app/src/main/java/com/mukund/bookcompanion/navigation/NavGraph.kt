package com.mukund.bookcompanion.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    BooksCompanionTheme() {
        Surface {
            NavHost(
                navController = navController,
                startDestination = BooksScreen.route
            ) {
                composable(
                    route = BooksScreen.route
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
                        }
                    )
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
                ) {
                    SettingScreen (
                        backPress = { navController.popBackStack() }
                    ) { navController.navigate(LibrariesScreen.route) }
                }
                composable(
                    route = LibrariesScreen.route
                ) {
                    LibsScreen {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}