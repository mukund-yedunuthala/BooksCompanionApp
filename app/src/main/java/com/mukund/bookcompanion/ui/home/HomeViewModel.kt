package com.mukund.bookcompanion.ui.home

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import com.mukund.bookcompanion.R
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.data.network.BooksDao
import com.mukund.bookcompanion.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val SORT_OPTION_KEY = stringPreferencesKey("sort_option")

enum class SortOption(@StringRes val labelRes: Int) {
    TITLE(R.string.sort_title),
    YEAR(R.string.sort_year),
}

/** Explicit load state for the single-book detail (Overview) screen. */
sealed interface BookDetailState {
    data object Loading : BookDetailState
    data object NotFound : BookDetailState
    data class Loaded(val book: Book) : BookDetailState
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val booksDao: BooksDao,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    var books: List<Book> by mutableStateOf(emptyList())
    var book by mutableStateOf(
        Book(id = 0,
            title = NO_VALUE,
            genre = NO_VALUE,
            isbn =  NO_VALUE,
            author = NO_VALUE,
            year = 0,
            status = NO_VALUE)
    )
        private set

    var sortOption by mutableStateOf(SortOption.TITLE)
        private set

    // Drives the Overview screen: Loading → Loaded(book) / NotFound. `book` above is retained for
    // the legacy field-update helpers; the detail UI reads this state instead.
    var bookDetail: BookDetailState by mutableStateOf(BookDetailState.Loading)
        private set

    private var booksFetchJob: Job? = null

    init {
        viewModelScope.launch {
            val stored = dataStore.data.map { it[SORT_OPTION_KEY] }.first()
            stored?.let { name ->
                SortOption.entries.firstOrNull { it.name == name }?.let { sortOption = it }
            }
            getBooks()
        }
    }

    fun updateSortOption(option: SortOption) {
        sortOption = option
        viewModelScope.launch { dataStore.edit { it[SORT_OPTION_KEY] = option.name } }
        getBooks()
    }

    fun getBook(id: Int) = viewModelScope.launch {
        bookDetail = BookDetailState.Loading
        booksDao.getBook(id).collectLatest { fetched ->
            if (fetched != null) {
                book = fetched
                bookDetail = BookDetailState.Loaded(fetched)
            } else {
                bookDetail = BookDetailState.NotFound
            }
        }
    }

    fun getBooks() {
        booksFetchJob?.cancel()
        booksFetchJob = viewModelScope.launch {
            when (sortOption) {
                SortOption.TITLE -> booksDao.getBooksSortedByTitle()
                SortOption.YEAR  -> booksDao.getBooksSortedByYear()
            }.collectLatest { this@BooksViewModel.books = it }
        }
    }

    fun addBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksDao.addBook(book)
    }

    fun updateBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksDao.updateBook(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        booksDao.deleteBook(book)
    }

    fun updateTitle(title: String) {
        book = book.copy(title = title)
    }

    fun updateGenre(genre: String) {
        book = book.copy(genre = genre)
    }

    fun updateISBN(isbn: String) {
        book = book.copy(isbn = isbn)
    }

    fun updateAuthor(author: String) {
        book = book.copy(author = author)
    }

    fun updateYear(year: String) {
        val parsed = year.toLongOrNull()
        if (parsed != null) {
            book = book.copy(year = parsed)
        }
    }

    fun updateStatus(status: String) {
        book = book.copy(status = status.trim())
    }

    fun insertAllBooks(books: List<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            booksDao.insertAll(books)
        }
    }

}
