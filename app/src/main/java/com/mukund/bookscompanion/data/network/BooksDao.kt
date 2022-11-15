package com.mukund.bookscompanion.data.network

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.mukund.bookscompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookscompanion.domain.model.Book
import com.mukund.bookscompanion.domain.repository.Books
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Query("SELECT * FROM $BOOK_TABLE ORDER BY id ASC")
    fun getBooks(): Flow<Books>

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id")
    fun getBook(id: Int): Book

    @Insert(onConflict = IGNORE)
    fun addBook(book: Book)

    @Update
    fun updateBook(book: Book)

    @Delete
    fun deleteBook(book: Book)
}