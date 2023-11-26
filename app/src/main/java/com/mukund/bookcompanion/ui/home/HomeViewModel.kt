package com.mukund.bookcompanion.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.data.repository.BooksRepositoryImpl
import com.mukund.bookcompanion.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
enum class SortOption( val displayName: String) {
    TITLE("Title"),
    YEAR("Year"),
    // Add more sort options as needed
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BooksRepositoryImpl,
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
    var openDialog by mutableStateOf(false)

    fun getBook(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        book = repository.getBookFromRoom(id)
    }

    fun getBooks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getBooksFromRoom().collectLatest {books ->
            this@BooksViewModel.books = books
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

//    fun updateGenre(genre: String) {
//        book = book.copy(genre = genre)
//    }
//
//    fun updateISBN(isbn: String) {
//        book = book.copy(isbn = isbn)
//    }

    fun updateAuthor(author: String) {
        book = book.copy(author = author)
    }

    fun updateYear(year: String) {
        if (year.isNotEmpty()) {
            book = book.copy(year = year.toLong())
        }
    }

    fun updateStatus(status: String) {
        book = book.copy(status = status.trim())
    }

    fun openDialog() {
        openDialog = true
    }

    fun closeDialog() {
        openDialog = false
    }

    fun insertAllBooks(books: List<Book>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllBooks(books)
        }
    }

}