package com.mukund.bookcompanion.ui.home.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mukund.bookcompanion.core.Constants.Companion.ADD
import com.mukund.bookcompanion.core.Constants.Companion.ADD_BOOK
import com.mukund.bookcompanion.core.Constants.Companion.AUTHOR
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TITLE
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.core.Constants.Companion.YEAR
import com.mukund.bookcompanion.core.Constants.Companion.GENRE
import com.mukund.bookcompanion.core.Constants.Companion.ISBN
import com.mukund.bookcompanion.domain.model.Book

@ExperimentalMaterial3Api
@Composable
fun BookAdditionDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addBook: (book: Book) -> Unit,
    haptic: HapticFeedback,
    books: List<Book>
) {
    val context = LocalContext.current
    if (openDialog) {
        var title by remember { mutableStateOf(NO_VALUE) }
        var author by remember { mutableStateOf(NO_VALUE) }
        var year by remember { mutableStateOf(NO_VALUE) }
        var genre by remember { mutableStateOf(NO_VALUE) }
        var isbn by remember { mutableStateOf(NO_VALUE) }
        var category by remember { mutableStateOf("Unread") }
        var dupeFlag = remember { mutableStateOf(false) }

        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = closeDialog,
        ) {
            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        title = {
                            Text(text = ADD_BOOK)
                        },
                        navigationIcon = {
                            IconButton(onClick = closeDialog) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Return to home screen"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    item {
                        CustomAdditionTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = title,
                            placeholder = BOOK_TITLE,
                            label = "Title",
                            onChange = {
                                title = it
                            }
                        )
                        CustomAdditionTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = author,
                            placeholder = AUTHOR,
                            label = "Author",
                            onChange = {
                                author = it
                            }
                        )
                        CustomAdditionTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = genre,
                            placeholder = GENRE,
                            label = "Genre",
                            onChange = {
                                genre = it
                            }
                        )
                        CustomAdditionTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = year,
                            placeholder = YEAR,
                            label = "Year of publication",
                            onChange = {
                                year = it
                            },
                            keyboardType = KeyboardType.Number
                        )
                        CustomAdditionTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = isbn,
                            placeholder = ISBN,
                            label = "ISBN",
                            onChange = {
                                isbn = it
                            },
                            keyboardType = KeyboardType.Number
                        )
                        CategoryRow(
                            current = category,
                            onCategorySelected = { selectedStatus ->
                                category = selectedStatus
                            }
                        )
                        Button(
                            onClick = {
                                dupeFlag.value = false
                                if (emptyCheck(title, author, year) && !dupeCheck(
                                        title,
                                        author,
                                        year,
                                        books,
                                        dupeFlag,
                                        context
                                    )
                                ) {
                                    val book = Book(
                                        0, title = title, author = author,
                                        genre = genre, isbn = isbn,
                                        year = year.toLong(),
                                        status = category)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    addBook(book)
                                    closeDialog()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(15.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Save this book",
                                modifier = Modifier
                                    .size(20.dp)
                                    .wrapContentWidth()
                            )
                            Text(
                                text = ADD.uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .wrapContentWidth(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


fun emptyCheck(title: String, author: String, year: String): Boolean {
    return (
            title.isNotEmpty() and
                    author.isNotEmpty() and
                    year.isNotEmpty()
            )
}

fun dupeCheck(
    title: String,
    author: String,
    year: String,
    allBooks: List<Book>,
    dupeFlag: MutableState<Boolean>,
    context: Context
): Boolean {

    for (book in allBooks) {
        if (
            (book.title == title) and
            (book.author == author) and
            (book.year.toString() == year)
        ) {
            dupeFlag.value = true
        }
    }
    if (dupeFlag.value) {
        Toast.makeText(context, "Book already exists!", Toast.LENGTH_SHORT).show()
    }
    return dupeFlag.value
}