package com.mukund.bookcompanion.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.domain.model.Book

@Database(entities = [Book::class], version = 3, exportSchema = false)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
        companion object {
        private const val FROM_VERSION = 2
        private const val TO_VERSION = 3

        val MIGRATION_2_3: Migration = object : Migration(FROM_VERSION, TO_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN genre TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN isbn TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}