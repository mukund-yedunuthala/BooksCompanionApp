package com.mukund.bookscompanion.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mukund.bookscompanion.core.Constants.Companion.ADD
import com.mukund.bookscompanion.core.Constants.Companion.ADD_BOOK
import com.mukund.bookscompanion.core.Constants.Companion.AUTHOR
import com.mukund.bookscompanion.core.Constants.Companion.BOOK_TITLE
import com.mukund.bookscompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookscompanion.core.Constants.Companion.YEAR
import com.mukund.bookscompanion.domain.model.Book
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun BookAdditionDialog(
    paddingValues: PaddingValues,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addBook: (book : Book) -> Unit
) {
    if (openDialog) {
        var title by remember { mutableStateOf(NO_VALUE) }
        var author by remember { mutableStateOf(NO_VALUE) }
        var year by remember { mutableStateOf(NO_VALUE) }

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
                        Button(
                            onClick = {
                                if (
                                    title.isNotEmpty() and
                                            author.isNotEmpty() and
                                            year.isNotEmpty()
                                        ) {
                                    val book = Book(0, title, author, year = year.toLong())
                                    addBook(book)
                                    closeDialog()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Save this book",
                                modifier = Modifier
                                    .size(ButtonDefaults.IconSize)
                                    .wrapContentWidth()
                            )
                            Text(
                                text = ADD.uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .wrapContentWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}