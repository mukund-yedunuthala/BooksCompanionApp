package com.mukund.bookcompanion.ui.edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.components.CustomAdditionTextField
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateContent(
    paddingValues: PaddingValues,
    book: Book,
    updateTitle: (title: String) -> Unit,
    updateAuthor: (author: String) -> Unit,
    updateYear: (year: String) -> Unit,
    updateGenre: (genre: String) -> Unit,
    updateISBN: (isbn: String) -> Unit,
    updateBook: (book: Book) -> Unit,
    backPress: () -> Unit,
    updateStatus: (String) -> Unit
) {
    var yearString by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    yearString = book.year.toString()
    category = book.status

    val readState = remember { mutableStateOf(false) }
    val unreadState = remember { mutableStateOf(false) }

    if (category == "Read") {
        readState.value = true
    }
    else { unreadState.value = true }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            CustomAdditionTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = book.title,
                placeholder = Constants.BOOK_TITLE,
                label = "Title",
                onChange = { title ->
                    updateTitle(title)
                }
            )
            CustomAdditionTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = book.author,
                placeholder = Constants.AUTHOR,
                label = "Author",
                onChange = { author ->
                    updateAuthor(author)
                }
            )
            CustomAdditionTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = book.genre,
                placeholder = Constants.GENRE,
                label = "Genre",
                onChange = { genre ->
                    updateGenre(genre)
                }
            )
            CustomAdditionTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = yearString,
                placeholder = Constants.YEAR,
                label = "Year",
                onChange = {
                    yearString = it
                    updateYear(yearString)
                },
                keyboardType = KeyboardType.Number
            )
            CustomAdditionTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = book.isbn,
                placeholder = Constants.ISBN,
                label = "ISBN",
                onChange = { isbn ->
                    updateISBN(isbn)
                },
                keyboardType = KeyboardType.Number
            )
            EditScreenButtonRow(updateStatus, readState, unreadState)
            Button(
                onClick = {
                    updateBook(book.copy(status = category))
                    backPress.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "Save this book",
                    modifier = Modifier
                        .size(20.dp)
                        .wrapContentWidth()
                )
                Text(
                    text = Constants.UPDATE.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .wrapContentWidth(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EditScreenButtonRow(updateStatus: (String) -> Unit, readState: MutableState<Boolean>, unreadState: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (readState.value) {
            EditFilledButton(
                text = "Read",
                onClick = {}
            )
            EditOutlinedButton(
                text = "Unread",
                onClick = {
                    updateStatus("Unread")
                    unreadState.value = !unreadState.value
                    readState.value = !readState.value
                }
            )
        }
        else {
            EditOutlinedButton(
                text = "Read",
                onClick = {
                    updateStatus("Read")
                    readState.value = !readState.value
                    unreadState.value = !unreadState.value
                }
            )
            EditFilledButton(
                text = "Unread",
                onClick = {}
            )
        }
    }
}

@Composable
fun EditFilledButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.filledTonalButtonColors(),
        contentPadding = ButtonDefaults.TextButtonContentPadding
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun EditOutlinedButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(),
        contentPadding = ButtonDefaults.TextButtonContentPadding
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
