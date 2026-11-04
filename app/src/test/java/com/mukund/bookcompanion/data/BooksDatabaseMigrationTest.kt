package com.mukund.bookcompanion.data

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import com.mukund.bookcompanion.data.network.BooksDatabase
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/** Verifies migration from version 1 runs end to end and old rows survive with sane defaults. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class BooksDatabaseMigrationTest {

    @Test
    fun migration_1_to_5_runsAndKeepsRows() = runBlocking {
        val context = RuntimeEnvironment.getApplication().applicationContext
        context.getDatabasePath("book_table").delete()

        // Schema as Room created it for the version-1 Book entity (Init commit).
        val dbFile = SQLiteDatabase.openOrCreateDatabase(
            context.getDatabasePath("book_table").absolutePath,
            null
        )
        dbFile.execSQL(
            "CREATE TABLE book_table (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "title TEXT NOT NULL, " +
                "author TEXT NOT NULL, " +
                "year INTEGER NOT NULL)"
        )
        val insert = dbFile.compileStatement(
            "INSERT INTO book_table (id, title, author, year) VALUES (?, ?, ?, ?)"
        )
        insert.bindLong(1, 1L)
        insert.bindString(2, "Old book")
        insert.bindString(3, "Old author")
        insert.bindLong(4, 2000L)
        insert.execute()
        dbFile.version = 1
        dbFile.close()

        val database = Room.databaseBuilder(context, BooksDatabase::class.java, "book_table")
            .addMigrations(
                BooksDatabase.MIGRATION_1_2,
                BooksDatabase.MIGRATION_2_3,
                BooksDatabase.MIGRATION_3_4,
                BooksDatabase.MIGRATION_4_5
            )
            .build()
        val books = database.booksDao().getBooksSortedByTitle().first()
        assertEquals(
            listOf(
                Book(
                    id = 1, title = "Old book", author = "Old author", year = 2000L,
                    status = "", genre = "", isbn = ""
                )
            ),
            books
        )
        database.close()
    }
}
