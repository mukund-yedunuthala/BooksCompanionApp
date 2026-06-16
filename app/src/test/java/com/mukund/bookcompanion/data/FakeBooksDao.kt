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
    var insertAllSignal = CompletableDeferred<List<Book>>()

    private val booksFlow = MutableStateFlow<List<Book>>(emptyList())
    private val bookFlow = MutableStateFlow<Book?>(null)

    fun emitBooks(books: List<Book>) { booksFlow.value = books }
    fun emitBook(book: Book?) { bookFlow.value = book }

    override fun getBooksSortedByTitle(): Flow<List<Book>> =
        booksFlow.map { it.sortedBy { b -> b.title } }

    override fun getBooksSortedByYear(): Flow<List<Book>> =
        booksFlow.map { it.sortedByDescending { b -> b.year } }

    override fun getBook(id: Int): Flow<Book?> = bookFlow
    override fun addBook(book: Book) { lastAdded = book }
    override fun insertAll(books: List<Book>) {
        lastInsertedAll = books
        insertAllSignal.complete(books)
    }
    override fun updateBook(book: Book) { lastUpdated = book }
    override fun deleteBook(book: Book) { lastDeleted = book }
}
