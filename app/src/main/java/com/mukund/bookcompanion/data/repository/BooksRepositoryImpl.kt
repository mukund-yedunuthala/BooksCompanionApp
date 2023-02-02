package com.mukund.bookcompanion.data.repository

import com.mukund.bookcompanion.data.network.BooksDao
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val booksDao: BooksDao
) : BooksRepository {
    override fun getBooksFromRoom() = booksDao.getBooks()

    override fun getBookFromRoom(id: Int) = booksDao.getBook(id)

    override fun addBookToRoom(book: Book) = booksDao.addBook(book)

    override fun updateBookInRoom(book: Book) = booksDao.updateBook(book)

    override fun deleteBookFromRoom(book: Book) = booksDao.deleteBook(book)
}