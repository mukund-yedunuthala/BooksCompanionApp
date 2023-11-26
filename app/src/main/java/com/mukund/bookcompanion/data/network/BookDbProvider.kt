package com.mukund.bookcompanion.data.network

import android.app.Application
import androidx.room.Room
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE

class BookDbProvider(
    private val application: Application
) {
    private var database: BooksDatabase? = null

    @Synchronized
    fun instance() : BooksDatabase {
        if (database == null) {
            database = Room.databaseBuilder(application, BooksDatabase::class.java, BOOK_TABLE)
                .addMigrations(BooksDatabase.MIGRATION_2_3)
                .build()
        }
        return database!!
    }

    @Synchronized
    fun close() {
        database?.close()
        database = null
    }

    fun BooksDao() : BooksDao {
        return instance().booksDao()
    }
}