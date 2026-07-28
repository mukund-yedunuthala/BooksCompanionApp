package com.mukund.bookcompanion.data.network

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.IGNORE
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TABLE
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Query("SELECT * FROM $BOOK_TABLE ORDER BY title ASC")
    fun getBooksSortedByTitle(): Flow<List<Book>>

    @Query("SELECT * FROM $BOOK_TABLE ORDER BY year DESC")
    fun getBooksSortedByYear(): Flow<List<Book>>

    @Query("SELECT * FROM $BOOK_TABLE WHERE id = :id")
    fun getBook(id: Int): Flow<Book?>

    @Insert(onConflict = IGNORE)
    fun addBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<Book>)

    @Update
    fun updateBook(book: Book)

    @Delete
    fun deleteBook(book: Book)
}
