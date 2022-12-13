package com.mukund.bookscompanion.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mukund.bookscompanion.domain.model.Book

@Database(entities = [Book::class], version = 2, exportSchema = false)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
}