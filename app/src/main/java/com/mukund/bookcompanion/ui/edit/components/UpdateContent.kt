package com.mukund.bookcompanion.ui.edit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.components.CustomAdditionTextField
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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
    val haptic = LocalHapticFeedback.current
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
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    updateBook(book.copy(status = category))
                    backPress.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = ButtonDefaults.IconSpacing)
                        .size(IconButtonDefaults.mediumIconSize)
                )
                Text(
                    text = Constants.UPDATE.uppercase(),
                    modifier = Modifier
                        .wrapContentWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditScreenButtonRow(
    updateStatus: (String) -> Unit,
    readState: MutableState<Boolean>,
    unreadState: MutableState<Boolean>
) {
    val selectedIndex = if (readState.value) 0 else 1
    val options = listOf("Read", "Unread")

    ButtonGroup(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        overflowIndicator = {}
    ) {
        options.forEachIndexed { index, label ->
            toggleableItem(
                weight = 1f,
                checked = selectedIndex == index,
                onCheckedChange = { checked ->
                    if (checked && selectedIndex != index) {
                        updateStatus(label)
                        readState.value = index == 0
                        unreadState.value = index == 1
                    }
                },
                label = label
            )
        }
    }
}