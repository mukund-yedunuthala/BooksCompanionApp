package com.mukund.bookcompanion.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BooksRepository
) : ViewModel() {

    var book by mutableStateOf(Book(0, NO_VALUE, NO_VALUE, 0, NO_VALUE))
        private set

    var openDialog by mutableStateOf(false)

    val books = repository.getBooksFromRoom()

    fun getBook(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        book = repository.getBookFromRoom(id)
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
}