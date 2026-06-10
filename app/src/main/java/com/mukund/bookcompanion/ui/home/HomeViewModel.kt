package com.mukund.bookcompanion.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val SORT_OPTION_KEY = stringPreferencesKey("sort_option")

enum class SortOption(val displayName: String) {
    TITLE("Title"),
    YEAR("Year"),
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BooksRepository,
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
        repository.getBookFromRoom(id).collectLatest { it?.let { book = it } }
    }

    fun getBooks() {
        booksFetchJob?.cancel()
        booksFetchJob = viewModelScope.launch {
            when (sortOption) {
                SortOption.TITLE -> repository.getBooksSortedByTitle()
                SortOption.YEAR  -> repository.getBooksSortedByYear()
            }.collectLatest { this@BooksViewModel.books = it }
        }
    }

    fun addBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.addBookToRoom(book)
    }

    fun updateBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateBookInRoom(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteBookFromRoom(book)
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
            repository.insertAllBooks(books)
        }
    }

}