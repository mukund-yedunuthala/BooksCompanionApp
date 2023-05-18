package com.mukund.bookcompanion.data.repository

import com.mukund.bookcompanion.data.network.BookDbProvider
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.BooksRepository

class BooksRepositoryImpl(
    private val provider: BookDbProvider
) : BooksRepository {
    override fun getBooksFromRoom() = provider.BooksDao().getBooks()

    override fun getBookFromRoom(id: Int) = provider.BooksDao().getBook(id)

    override fun addBookToRoom(book: Book) = provider.BooksDao().addBook(book)

    override fun updateBookInRoom(book: Book) = provider.BooksDao().updateBook(book)

    override fun deleteBookFromRoom(book: Book) = provider.BooksDao().deleteBook(book)
    override fun insertAllBooks(books: List<Book>) = provider.BooksDao().insertAll(books)

}