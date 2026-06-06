package com.mukund.bookcompanion.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.domain.model.Book

@Database(entities = [Book::class], version = 4, exportSchema = false)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
        companion object {
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN genre TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN isbn TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $BOOK_TABLE ADD COLUMN rating INTEGER")
            }
        }
    }
}