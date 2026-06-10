package com.mukund.bookcompanion.data

import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.BooksRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBooksRepository : BooksRepository {
    var lastAdded: Book? = null
    var lastDeleted: Book? = null
    var lastUpdated: Book? = null
    var lastInsertedAll: List<Book>? = null

    /** Awaitable signal for insertAllBooks — complete once, then replace if needed. */
    var insertAllSignal = CompletableDeferred<List<Book>>()

    private val booksFlow = MutableStateFlow<List<Book>>(emptyList())
    private val bookFlow = MutableStateFlow<Book?>(null)

    fun emitBooks(books: List<Book>) { booksFlow.value = books }
    fun emitBook(book: Book?) { bookFlow.value = book }

    override fun getBooksSortedByTitle(): Flow<List<Book>> =
        booksFlow.map { it.sortedBy { b -> b.title } }

    override fun getBooksSortedByYear(): Flow<List<Book>> =
        booksFlow.map { it.sortedByDescending { b -> b.year } }
    override fun getBookFromRoom(id: Int): Flow<Book?> = bookFlow
    override fun addBookToRoom(book: Book) { lastAdded = book }
    override fun updateBookInRoom(book: Book) { lastUpdated = book }
    override fun deleteBookFromRoom(book: Book) { lastDeleted = book }
    override fun insertAllBooks(books: List<Book>) {
        lastInsertedAll = books
        insertAllSignal.complete(books)
    }
}
