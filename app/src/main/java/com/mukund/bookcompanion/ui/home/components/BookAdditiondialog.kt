package com.mukund.bookcompanion.ui.home.components

import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.theme.AppType
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

    var title       by rememberSaveable { mutableStateOf(bookToEdit?.title        ?: "") }
    var author      by rememberSaveable { mutableStateOf(bookToEdit?.author       ?: "") }
    var year        by rememberSaveable { mutableStateOf(bookToEdit?.year?.toString() ?: "") }
    var genre       by rememberSaveable { mutableStateOf(bookToEdit?.genre        ?: "") }
    var isbn        by rememberSaveable { mutableStateOf(bookToEdit?.isbn         ?: "") }
    var category    by rememberSaveable { mutableStateOf(bookToEdit?.status       ?: "Unread") }
    var language    by rememberSaveable { mutableStateOf(bookToEdit?.language     ?: "") }
    var notes       by rememberSaveable { mutableStateOf(bookToEdit?.notes        ?: "") }
    var dateStarted by rememberSaveable { mutableStateOf(bookToEdit?.dateStarted  ?: "") }
    var dateFinished by rememberSaveable { mutableStateOf(bookToEdit?.dateFinished ?: "") }

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
            title.isNotBlank() && author.isNotBlank() && !isDuplicate
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
                .imePadding()
                .padding(horizontal = BookCompanionSpacing.gutter)
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
                            text = stringResource(
                                if (isEditMode) R.string.sheet_edit_eyebrow
                                else R.string.sheet_new_eyebrow
                            ).uppercase(),
                            style = AppType.labelMicroMono,
                            color = bookColors.inkFaint,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(
                                if (isEditMode) R.string.sheet_edit_title
                                else R.string.sheet_new_title
                            ),
                            style = AppType.titleSerif,
                            color = bookColors.ink,
                        )
                    }
                }

                HorizontalDivider(
                    color = bookColors.rule,
                    thickness = BookCompanionBorders.hairline,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // ── Fields ────────────────────────────────────
                EditorialTextField(
                    value = title,
                    label = stringResource(R.string.sheet_field_title).uppercase(),
                    placeholder = stringResource(R.string.sheet_placeholder_title),
                    onChange = { title = it },
                    useSerif = true,
                    required = true,
                )
                EditorialTextField(
                    value = author,
                    label = stringResource(R.string.sheet_field_author).uppercase(),
                    placeholder = stringResource(R.string.sheet_placeholder_author),
                    onChange = { author = it },
                    required = true,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EditorialTextField(
                        value = year,
                        label = stringResource(R.string.sheet_field_year).uppercase(),
                        placeholder = stringResource(R.string.sheet_placeholder_year),
                        onChange = { year = it },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    EditorialTextField(
                        value = genre,
                        label = stringResource(R.string.sheet_field_genre).uppercase(),
                        placeholder = stringResource(R.string.sheet_placeholder_genre),
                        onChange = { genre = it },
                        modifier = Modifier.weight(2f)
                    )
                }
                EditorialTextField(
                    value = isbn,
                    label = stringResource(R.string.sheet_field_isbn).uppercase(),
                    placeholder = stringResource(R.string.sheet_placeholder_isbn),
                    onChange = { isbn = it },
                    keyboardType = KeyboardType.Ascii,
                )
                EditorialTextField(
                    value = language,
                    label = stringResource(R.string.sheet_field_language).uppercase(),
                    placeholder = stringResource(R.string.sheet_placeholder_language),
                    onChange = { language = it },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    EditorialDateField(
                        value = dateStarted,
                        label = stringResource(R.string.sheet_field_started).uppercase(),
                        placeholder = stringResource(R.string.sheet_placeholder_date),
                        onChange = { dateStarted = it },
                        modifier = Modifier.weight(1f)
                    )
                    EditorialDateField(
                        value = dateFinished,
                        label = stringResource(R.string.sheet_field_finished).uppercase(),
                        placeholder = stringResource(R.string.sheet_placeholder_date),
                        onChange = { dateFinished = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                EditorialTextField(
                    value = notes,
                    label = stringResource(R.string.sheet_field_notes).uppercase(),
                    placeholder = stringResource(R.string.sheet_placeholder_notes),
                    onChange = { notes = it },
                    singleLine = false,
                )

                Spacer(Modifier.height(8.dp))

                // ── Status ────────────────────────────────────
                Text(
                    text = stringResource(R.string.sheet_field_status).uppercase(),
                    style = AppType.labelMicroMono,
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
                    text = if (isDuplicate) stringResource(R.string.sheet_duplicate, title)
                    else stringResource(R.string.sheet_required_hint),
                    style = AppType.labelSmallLight,
                    color = if (isDuplicate) bookColors.terracotta
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
                        .clickable(enabled = isValid, role = Role.Button) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            if (isEditMode) {
                                updateBook(
                                    bookToEdit!!.copy(
                                        title        = title,
                                        author       = author,
                                        genre        = genre,
                                        isbn         = isbn,
                                        year         = year.toLongOrNull() ?: bookToEdit.year,
                                        status       = category,
                                        language     = language.trimToNull(),
                                        notes        = notes.trimToNull(),
                                        dateStarted  = dateStarted.trimToNull(),
                                        dateFinished = dateFinished.trimToNull(),
                                    )
                                )
                            } else {
                                addBook(
                                    Book(
                                        id           = 0,
                                        title        = title,
                                        author       = author,
                                        genre        = genre,
                                        isbn         = isbn,
                                        year         = year.toLongOrNull() ?: 0L,
                                        status       = category,
                                        language     = language.trimToNull(),
                                        notes        = notes.trimToNull(),
                                        dateStarted  = dateStarted.trimToNull(),
                                        dateFinished = dateFinished.trimToNull(),
                                    )
                                )
                            }
                            closeSheet()
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            if (isEditMode) R.string.sheet_submit_save
                            else R.string.sheet_submit_add
                        ),
                        style = AppType.sheetActionSerif,
                        color = if (isValid) bookColors.paper else bookColors.inkFaint,
                    )
                }
            }
        }
    }
}

private fun String.trimToNull(): String? = trim().ifBlank { null }