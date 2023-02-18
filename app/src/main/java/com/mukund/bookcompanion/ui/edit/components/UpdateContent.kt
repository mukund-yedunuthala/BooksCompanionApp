package com.mukund.bookcompanion.ui.edit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.core.Constants
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.components.CategoryRow
import com.mukund.bookcompanion.ui.home.components.CustomAdditionTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateContent(
    paddingValues: PaddingValues,
    book: Book,
    updateTitle: (title: String) -> Unit,
    updateAuthor: (author: String) -> Unit,
    updateYear: (year: String) -> Unit,
    updateBook: (book: Book) -> Unit,
    backPress: () -> Unit,
    updateStatus: (String) -> Unit
) {
    var yearString by rememberSaveable { mutableStateOf("") }
    yearString = book.year.toString()
    var status : String?
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
                onChange = {title ->
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
                onChange = {author ->
                    updateAuthor(author)
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
            status = CategoryRow(current = book.status)
            status?.let { updateStatus(it) }
            Button(
                onClick = {
                    updateBook(book)
                    backPress.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
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