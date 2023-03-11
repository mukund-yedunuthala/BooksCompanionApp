package com.mukund.bookcompanion.navigation

import com.mukund.bookcompanion.core.Constants.Companion.BACKUP_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.BOOKS_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.LIBRARIES_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.OVERVIEW_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.SETTINGS_SCREEN
import com.mukund.bookcompanion.core.Constants.Companion.UPDATE_BOOK_SCREEN

sealed class Screen(val route: String) {
    object BooksScreen: Screen(BOOKS_SCREEN)
    object UpdateBookScreen: Screen(UPDATE_BOOK_SCREEN)
    object SettingsScreen: Screen(SETTINGS_SCREEN)
    object LibrariesScreen: Screen(LIBRARIES_SCREEN)
    object OverviewScreen: Screen(OVERVIEW_SCREEN)
    object BackupScreen: Screen(BACKUP_SCREEN)
}