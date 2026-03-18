package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.AUTHOR
import com.mukund.bookcompanion.core.Constants.Companion.BOOK_TITLE
import com.mukund.bookcompanion.core.Constants.Companion.GENRE
import com.mukund.bookcompanion.core.Constants.Companion.ISBN
import com.mukund.bookcompanion.core.Constants.Companion.YEAR
import com.mukund.bookcompanion.domain.model.Book
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookAdditionBottomSheet(
    onDismiss: () -> Unit,
    addBook: (book: Book) -> Unit,
    books: List<Book>,
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title   by rememberSaveable { mutableStateOf("") }
    var author  by rememberSaveable { mutableStateOf("") }
    var year    by rememberSaveable { mutableStateOf("") }
    var genre   by rememberSaveable { mutableStateOf("") }
    var isbn    by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Unread") }

    val isDuplicate by remember {
        derivedStateOf {
            books.any { book ->
                book.title.equals(title, ignoreCase = true) &&
                        book.author.equals(author, ignoreCase = true) &&
                        book.year.toString() == year
            }
        }
    }
    val isValid by remember {
        derivedStateOf {
            title.isNotBlank() && author.isNotBlank() && year.isNotBlank() && !isDuplicate
        }
    }

    val closeSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) onDismiss()
        }
    }

    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 6.dp)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 32.dp)
        ) {
            item {
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = title,
                    placeholder = BOOK_TITLE,
                    label = "Title *",
                    onChange = { title = it }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = author,
                    placeholder = AUTHOR,
                    label = "Author *",
                    onChange = { author = it }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = genre,
                    placeholder = GENRE,
                    label = "Genre",
                    onChange = { genre = it }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = year,
                    placeholder = YEAR,
                    label = "Year of publication *",
                    onChange = { year = it },
                    keyboardType = KeyboardType.Number
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = isbn,
                    placeholder = ISBN,
                    label = "ISBN",
                    onChange = { isbn = it },
                    keyboardType = KeyboardType.Number
                )

                CategoryButtonGroup(
                    currentCategory = category,
                    onCategorySelect = { category = it },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )

                val messageText = if (isDuplicate) "$title already exists!"
                else "Fields marked with * are required."
                Text(
                    text = messageText,
                    style = MaterialTheme.typography.bodySmallEmphasized,
                    color = if (isDuplicate) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        addBook(
                            Book(
                                id = 0,
                                title = title,
                                author = author,
                                genre = genre,
                                isbn = isbn,
                                year = year.toLongOrNull() ?: 0L,
                                status = category
                            )
                        )
                        closeSheet()
                    },
                    enabled = isValid,
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
                    Text(text = "Add Book")
                }
            }
        }
    }
}
