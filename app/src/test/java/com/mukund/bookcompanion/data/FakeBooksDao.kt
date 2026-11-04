package com.mukund.bookcompanion.data

import com.mukund.bookcompanion.data.network.BooksDao
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBooksDao : BooksDao {
    var lastAdded: Book? = null
    var lastDeleted: Book? = null
    var lastUpdated: Book? = null
    var lastInsertedAll: List<Book>? = null
    var insertAllFailure: Throwable? = null
    var insertAllSignal = CompletableDeferred<List<Book>>()

    private val booksFlow = MutableStateFlow<List<Book>>(emptyList())
    private var lastEmittedBook: Book? = null
    private val bookFlows = mutableMapOf<Int, MutableStateFlow<Book?>>()

    fun emitBooks(books: List<Book>) { booksFlow.value = books }
    fun emitBook(book: Book?) {
        lastEmittedBook = book
        if (book == null) bookFlows.values.forEach { it.value = null }
        else bookFlows[book.id]?.value = book
    }
    fun emitBook(id: Int, book: Book?) {
        bookFlows.getOrPut(id) { MutableStateFlow(null) }.value = book
    }

    override fun getBooksSortedByTitle(): Flow<List<Book>> =
        booksFlow.map { it.sortedBy { b -> b.title.lowercase() } }

    override fun getBooksSortedByYear(): Flow<List<Book>> =
        booksFlow.map { it.sortedByDescending { b -> b.year } }

    override fun getBook(id: Int): Flow<Book?> =
        bookFlows.getOrPut(id) { MutableStateFlow(lastEmittedBook?.takeIf { it.id == id }) }
    override fun addBook(book: Book) { lastAdded = book }
    override fun insertAll(books: List<Book>) {
        insertAllFailure?.let { throw it }
        lastInsertedAll = books
        insertAllSignal.complete(books)
    }
    override fun updateBook(book: Book) { lastUpdated = book }
    override fun deleteBook(book: Book) { lastDeleted = book }
}
