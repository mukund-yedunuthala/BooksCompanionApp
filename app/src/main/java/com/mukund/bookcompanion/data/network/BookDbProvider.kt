package com.mukund.bookcompanion.data.network

import android.app.Application
import androidx.room.Room
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE

class BookDbProvider(
    private val application: Application
) {
    private val database: BooksDatabase by lazy {
        Room.databaseBuilder(application, BooksDatabase::class.java, BOOK_TABLE)
            .addMigrations(BooksDatabase.MIGRATION_2_3, BooksDatabase.MIGRATION_3_4, BooksDatabase.MIGRATION_4_5)
            .build()
    }

    fun BooksDao(): BooksDao = database.booksDao()
}