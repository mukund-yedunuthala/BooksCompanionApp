package com.mukund.bookcompanion.core

import com.mukund.bookcompanion.domain.model.Book


class Constants {
    companion object {
        //Room
        const val BOOK_TABLE = "book_table"

        //Screens
        const val BOOKS_SCREEN = "Books"
        const val UPDATE_BOOK_SCREEN = "Update book"
        const val SETTINGS_SCREEN = "Settings"
        const val LIBRARIES_SCREEN = "Libraries"
        const val OVERVIEW_SCREEN = "Overview"
        const val BACKUP_SCREEN = "Backup"


        //Arguments
        const val BOOK_ID = "bookId"

        //Actions
        const val ADD_BOOK = "Add a book"
        const val DELETE_BOOK = "Delete a book."

        //Buttons
        const val ADD = "Add"
        const val DISMISS = "Dismiss"
        const val UPDATE = "Update"

        //Placeholders
        const val BOOK_TITLE = "Type a book title..."
        const val AUTHOR = "Type the author name..."
        const val YEAR = "Type the year of publication..."
        const val NO_VALUE = ""
        const val GENRE = "Enter the genre here"
        const val ISBN = "Enter the ISBN here"

        val testBook: Book = Book(
            id = 0,
            title = "Test book",
            author = "Test book author",
            year = 2025,
            status = "Read",
            genre = "Fiction",
            isbn = "123456"
        )
    }
}