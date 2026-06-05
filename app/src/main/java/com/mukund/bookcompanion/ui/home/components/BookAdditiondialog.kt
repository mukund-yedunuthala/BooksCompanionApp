package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.design.CormorantGaramond
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.design.JetBrainsMono
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.theme.bookColors
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookAdditionBottomSheet(
    onDismiss: () -> Unit,
    addBook: (book: Book) -> Unit,
    updateBook: (book: Book) -> Unit,
    books: List<Book>,
    bookToEdit: Book? = null,
) {
    val isEditMode = bookToEdit != null
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title    by rememberSaveable { mutableStateOf(bookToEdit?.title   ?: "") }
    var author   by rememberSaveable { mutableStateOf(bookToEdit?.author  ?: "") }
    var year     by rememberSaveable { mutableStateOf(bookToEdit?.year?.toString() ?: "") }
    var genre    by rememberSaveable { mutableStateOf(bookToEdit?.genre   ?: "") }
    var isbn     by rememberSaveable { mutableStateOf(bookToEdit?.isbn    ?: "") }
    var category by rememberSaveable { mutableStateOf(bookToEdit?.status  ?: "Unread") }

    val isDuplicate by remember {
        derivedStateOf {
            if (isEditMode) false  // editing existing — skip duplicate check
            else books.any { book ->
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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = bookColors.paper,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 4.dp)
                    .width(32.dp)
                    .height(3.dp)
                    .background(
                        color = bookColors.rule,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .padding(bottom = 32.dp)
        ) {
            item {

                // ── Sheet header ──────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = if (isEditMode) "EDIT ENTRY" else "NEW ENTRY",
                            fontFamily = JetBrainsMono,
                            fontSize = 9.sp,
                            letterSpacing = 0.16.sp,
                            color = bookColors.inkFaint,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (isEditMode) "Revise details" else "Shelve a book",
                            fontFamily = CormorantGaramond,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            letterSpacing = (-0.01).sp,
                            color = bookColors.ink,
                        )
                    }
                }

                HorizontalDivider(
                    color = bookColors.rule,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // ── Fields ────────────────────────────────────
                EditorialTextField(
                    value = title,
                    label = "TITLE",
                    placeholder = "The Overstory",
                    onChange = { title = it },
                    useSerif = true,
                )
                EditorialTextField(
                    value = author,
                    label = "AUTHOR",
                    placeholder = "Richard Powers",
                    onChange = { author = it },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EditorialTextField(
                        value = year,
                        label = "YEAR",
                        placeholder = "2018",
                        onChange = { year = it },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    EditorialTextField(
                        value = genre,
                        label = "GENRE",
                        placeholder = "Literary Fiction",
                        onChange = { genre = it },
                        modifier = Modifier.weight(2f)
                    )
                }
                EditorialTextField(
                    value = isbn,
                    label = "ISBN",
                    placeholder = "978-3-16-148410-0",
                    onChange = { isbn = it },
                    keyboardType = KeyboardType.Number,
                )

                Spacer(Modifier.height(8.dp))

                // ── Status ────────────────────────────────────
                Text(
                    text = "STATUS",
                    fontFamily = JetBrainsMono,
                    fontSize = 9.sp,
                    letterSpacing = 0.16.sp,
                    color = bookColors.inkFaint,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CategoryButtonGroupNew(
                    currentCategory = category,
                    onCategorySelect = { category = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // ── Validation message ────────────────────────
                Text(
                    text = if (isDuplicate) "$title already exists!"
                    else "Fields marked with * are required.",
                    fontFamily = IBMPlexSans,
                    fontSize = 12.sp,
                    letterSpacing = 0.02.sp,
                    color = if (isDuplicate) MaterialTheme.colorScheme.error
                    else bookColors.inkFaint,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Spacer(Modifier.height(8.dp))

                // ── Submit button ─────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(
                            color = if (isValid) bookColors.ink else bookColors.rule
                        )
                        .clickable(enabled = isValid) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            if (isEditMode) {
                                updateBook(
                                    bookToEdit!!.copy(
                                        title  = title,
                                        author = author,
                                        genre  = genre,
                                        isbn   = isbn,
                                        year   = year.toLongOrNull() ?: bookToEdit.year,
                                        status = category,
                                    )
                                )
                            } else {
                                addBook(
                                    Book(
                                        id     = 0,
                                        title  = title,
                                        author = author,
                                        genre  = genre,
                                        isbn   = isbn,
                                        year   = year.toLongOrNull() ?: 0L,
                                        status = category,
                                    )
                                )
                            }
                            closeSheet()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEditMode) "Save changes →" else "Add to library →",
                        fontFamily = CormorantGaramond,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        color = if (isValid) bookColors.paper else bookColors.inkFaint,
                    )
                }
            }
        }
    }
}