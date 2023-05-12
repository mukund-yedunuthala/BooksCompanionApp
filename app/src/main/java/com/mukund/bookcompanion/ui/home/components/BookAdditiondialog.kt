package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mukund.bookcompanion.core.Constants.Companion.ADD
import com.mukund.bookcompanion.core.Constants.Companion.ADD_BOOK
import com.mukund.bookcompanion.core.Constants.Companion.AUTHOR
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TITLE
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.core.Constants.Companion.YEAR
import com.mukund.bookcompanion.domain.model.Book
@ExperimentalMaterial3Api
@Composable
fun BookAdditionDialog(
    paddingValues: PaddingValues,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addBook: (book: Book) -> Unit,
    haptic: HapticFeedback,
    books: List<Book>
) {
    if (openDialog) {
        var title by remember { mutableStateOf(NO_VALUE) }
        var author by remember { mutableStateOf(NO_VALUE) }
        var year by remember { mutableStateOf(NO_VALUE) }
        var category by remember { mutableStateOf(NO_VALUE) }
        var dupeFlag = remember { mutableStateOf(false) }

        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = closeDialog,
        )
        {
            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        title = {
                            Text(text = ADD_BOOK)
                        },
                        navigationIcon = {
                            IconButton(onClick = closeDialog) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Return to home screen"
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(Modifier.padding(paddingValues)) {
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
                            text = year,
                            placeholder = YEAR,
                            label = "Year",
                            onChange = {
                                year = it
                            },
                            keyboardType = KeyboardType.Number
                        )
                        CategoryRow().let { category = it }
                        Button(
                            onClick = {
                                dupeFlag.value = false
                                if (
                                    emptyCheck(title, author, year) and
                                            !dupeCheck(title, author, year, books, dupeFlag)
                                        ) {
                                    val book = Book(0, title, author, year = year.toLong(), status = category)
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
    dupeFlag: MutableState<Boolean>
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
        /*TODO*/
    }
    return dupeFlag.value
}