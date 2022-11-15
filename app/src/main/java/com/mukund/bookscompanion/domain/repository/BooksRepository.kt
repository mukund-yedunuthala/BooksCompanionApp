package com.mukund.bookscompanion.domain.repository

import com.mukund.bookscompanion.domain.model.Book
import kotlinx.coroutines.flow.Flow

typealias Books = List<Book>

interface BooksRepository {
    fun getBooksFromRoom(): Flow<Books>

    fun getBookFromRoom(id: Int): Book

    fun addBookToRoom(book: Book)

    fun updateBookInRoom(book: Book)

    fun deleteBookFromRoom(book: Book)
}