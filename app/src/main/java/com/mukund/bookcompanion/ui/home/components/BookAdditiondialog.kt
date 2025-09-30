package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
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
    showSheet: Boolean,
    onDismiss: () -> Unit,
    addBook: (book: Book) -> Unit,
    haptic: HapticFeedback,
    books: List<Book>,
) {
    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (!showSheet) {
        LaunchedEffect(sheetState.isVisible) {
            if (sheetState.isVisible) sheetState.hide()
        }
        return
    }
    val isDuplicateFlag = remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        var title by remember { mutableStateOf("") }
        var author by remember { mutableStateOf("") }
        var year by remember { mutableStateOf("") }
        var genre by remember { mutableStateOf("") }
        var isbn by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Unread") }

        val closeSheet: () -> Unit = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismiss()
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 32.dp)
        ) {
            item {
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = title,
                    placeholder = BOOK_TITLE,
                    label = "Title *",
                    onChange = {
                        title = it
                    }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = author,
                    placeholder = AUTHOR,
                    label = "Author *",
                    onChange = {
                        author = it
                    }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = genre,
                    placeholder = GENRE,
                    label = "Genre",
                    onChange = {
                        genre = it
                    }
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = year,
                    placeholder = YEAR,
                    label = "Year of publication *",
                    onChange = {
                        year = it
                    },
                    keyboardType = KeyboardType.Number
                )
                CustomAdditionTextField(
                    modifier = textFieldModifier,
                    text = isbn,
                    placeholder = ISBN,
                    label = "ISBN",
                    onChange = {
                        isbn = it
                    },
                    keyboardType = KeyboardType.Number
                )
                CategoryButtonGroup(
                    currentCategory = category,
                    onCategorySelect = { newCategory ->
                        category = newCategory
                    },
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                SaveButton(
                    enabled = isBookValid(
                        title, author, year, books, isDuplicateFlag),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    onClick = {
                        val book = Book(
                            id = 0,
                            title = title,
                            author = author,
                            genre = genre,
                            isbn = isbn,
                            year = year.toLongOrNull() ?: 0L, // Safe conversion
                            status = category
                        )
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        addBook(book)
                        closeSheet()
                    }
                )
                var messageText: String
                val messageColors: Color
                if (isDuplicateFlag.value) {
                    messageText = "$title already exists!"
                    messageColors = MaterialTheme.colorScheme.error
                } else {
                    messageText = "Fields marked with * are required."
                    messageColors = MaterialTheme.colorScheme.onSurface
                }
                Row(
                    modifier = textFieldModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                    ) {
                    Text(
                        text = messageText,
                        style = MaterialTheme.typography.bodySmallEmphasized,
                        modifier = Modifier.padding(start = 10.dp),
                        color = messageColors
                    )
                }
            }
        }
    }
}

private fun isBookValid(
    title: String,
    author: String,
    year: String,
    allBooks: List<Book>,
    isDuplicateFlag: MutableState<Boolean>,
): Boolean {
    if (title.isBlank() || author.isBlank() || year.isBlank()) {
        return false
    }

    val isDuplicate = allBooks.any { book ->
        book.title.equals(title, ignoreCase = true) &&
                book.author.equals(author, ignoreCase = true) &&
                book.year.toString() == year
    }

    if (isDuplicate) {
        isDuplicateFlag.value = true
        return false
    }

    return true
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors: IconButtonColors = if (!enabled) {
        IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    } else {
        IconButtonDefaults.filledIconButtonColors()
    }
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = colors
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text(
                text = "ADD BOOK",
            )
        }
    }
}
