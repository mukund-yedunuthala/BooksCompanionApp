package com.mukund.bookscompanion.data.repository

import com.mukund.bookscompanion.data.network.BooksDao
import com.mukund.bookscompanion.domain.model.Book
import com.mukund.bookscompanion.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val booksDao: BooksDao
) : BooksRepository {
    override fun getBooksFromRoom() = booksDao.getBooks()

    override fun getBookFromRoom(id: Int) = booksDao.getBook(id)

    override fun addBookToRoom(book: Book) = booksDao.addBook(book)

    override fun updateBookInRoom(book: Book) = booksDao.updateBook(book)

    override fun deleteBookFromRoom(book: Book) = booksDao.deleteBook(book)
}