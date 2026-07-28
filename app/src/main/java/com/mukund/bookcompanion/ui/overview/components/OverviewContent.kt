package com.mukund.bookcompanion.ui.overview.components

import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.home.components.BookAdditionBottomSheet
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewContent(
    modifier: Modifier = Modifier,
    book: Book,
    onDeleteBook: (Book) -> Unit,
    onBackPress: () -> Boolean,
    onUpdateBook: (Book) -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = BookCompanionSpacing.gutter)
            .padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Title block ───────────────────────────────────────
        if (book.genre != NO_VALUE) {
            Text(
                text = book.genre.trim().uppercase(),
                style = AppType.labelMicroMono,
                color = bookColors.inkFaint,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Text(
            text = book.title.trim(),
            style = AppType.titleSerifLarge,
            color = bookColors.ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.overview_by_author, book.author.trim()),
            style = AppType.authorSerifItalicLarge,
            color = bookColors.inkSoft,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ── Status badge ──────────────────────────────────────
        OverviewStatusBadge(
            status = book.status,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ── Rating ────────────────────────────────────────────
        val currentRating = book.rating ?: 0
        val ratingDescription = stringResource(R.string.overview_rating_description, currentRating)
        Row(
            modifier = Modifier
                .padding(bottom = BookCompanionSpacing.gutter)
                .semantics { contentDescription = ratingDescription }
        ) {
            (1..5).forEach { star ->
                val filled = currentRating >= star
                // Tapping the current top star clears the rating; otherwise sets it.
                val clearing = currentRating == star
                val clickLabel = if (clearing) {
                    stringResource(R.string.overview_clear_rating)
                } else {
                    stringResource(R.string.overview_rate_stars, star)
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable(role = Role.Button, onClickLabel = clickLabel) {
                            onUpdateBook(book.copy(rating = if (clearing) null else star))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = null,
                        tint = if (filled) bookColors.terracotta else bookColors.inkFaint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // ── Divider ───────────────────────────────────────────
        HorizontalDivider(
            color = bookColors.rule,
            thickness = BookCompanionBorders.hairline,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // ── Metadata rows ─────────────────────────────────────
        if (book.year != 0L) {
            MetaRow(label = stringResource(R.string.overview_meta_year), value = book.year.toString())
        }
        if (!book.language.isNullOrBlank()) {
            MetaRow(label = stringResource(R.string.overview_meta_language), value = book.language)
        }
        if (book.isbn != NO_VALUE) {
            MetaRow(label = stringResource(R.string.overview_meta_isbn), value = book.isbn, mono = true)
        }
        if (!book.dateStarted.isNullOrBlank()) {
            MetaRow(label = stringResource(R.string.overview_meta_started), value = book.dateStarted)
        }
        if (!book.dateFinished.isNullOrBlank()) {
            MetaRow(label = stringResource(R.string.overview_meta_finished), value = book.dateFinished)
        }

        // ── Notes ─────────────────────────────────────────────
        if (!book.notes.isNullOrBlank()) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .fillMaxHeight()
                        .background(bookColors.rule)
                )
                Text(
                    text = book.notes,
                    style = AppType.authorSerifItalicLarge,
                    color = bookColors.inkSoft,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        // ── Action buttons ────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Edit
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(color = bookColors.ink)
                    .clickable(role = Role.Button) { showEditSheet = true }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = stringResource(R.string.edit),
                        tint = bookColors.paper,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = stringResource(R.string.edit),
                        style = AppType.labelMicroMono,
                        color = bookColors.paper,
                    )
                }
            }

            // Delete
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(width = BookCompanionBorders.hairline, color = bookColors.rule)
                    .clickable(role = Role.Button) { showDeleteDialog = true }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.delete),
                        tint = bookColors.inkSoft,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = stringResource(R.string.delete),
                        style = AppType.labelMicroMono,
                        color = bookColors.inkSoft,
                    )
                }
            }
        }

        // ── Delete confirmation ───────────────────────────────
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = bookColors.paper,
                title = {
                    Text(
                        text = stringResource(R.string.delete_dialog_title),
                        style = AppType.titleSerif,
                        color = bookColors.ink,
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.delete_dialog_confirm_body, book.title.trim()),
                        style = AppType.bodySmall,
                        color = bookColors.inkSoft,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDeleteBook(book)
                            onBackPress()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.delete_confirm),
                            style = AppType.labelSmall,
                            color = bookColors.terracotta,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = AppType.labelSmall,
                            color = bookColors.inkSoft,
                        )
                    }
                }
            )
        }
        if (showEditSheet) {
            BookAdditionBottomSheet(
                onDismiss = { showEditSheet = false },
                addBook = {},           // not used in edit mode
                // Just persist; the sheet closes itself and the getBook flow refreshes this
                // screen in place. (Previously this also popped back to Home, hiding the edit.)
                updateBook = { updated -> onUpdateBook(updated) },
                books = emptyList(),    // not used in edit mode — duplicate check disabled
                bookToEdit = book,
            )
        }
    }
}

// ── Status badge ──────────────────────────────────────────────────────────────
@Composable
private fun OverviewStatusBadge(
    status: String,
    modifier: Modifier = Modifier,
) {
    val dotColor = when (status.lowercase()) {
        "read"    -> bookColors.sage
        "reading" -> bookColors.ochre
        else      -> Color.Transparent
    }
    val isUnread = status.lowercase() == "unread"
    val borderColor = if (isUnread) bookColors.inkFaint else dotColor

    Row(
        modifier = modifier
            .border(width = BookCompanionBorders.hairline, color = borderColor)
            .padding(horizontal = 16.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .border(
                    width = 1.dp,
                    color = if (isUnread) bookColors.inkFaint else Color.Transparent,
                    shape = CircleShape
                )
                .background(dotColor, CircleShape)
        )
        Text(
            text = status.trim().uppercase(),
            style = AppType.labelMicroMono,
            color = if (isUnread) bookColors.inkSoft else bookColors.ink,
        )
    }
}

// ── Metadata row ──────────────────────────────────────────────────────────────
@Composable
private fun MetaRow(
    label: String,
    value: String,
    mono: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = label.uppercase(),
            style = AppType.labelMicroMono,
            color = bookColors.inkFaint,
        )
        Text(
            text = value,
            style = if (mono) AppType.labelSmall else AppType.body,
            color = bookColors.inkSoft,
        )
    }
    HorizontalDivider(color = bookColors.ruleSoft, thickness = BookCompanionBorders.hairline)
}
@Composable
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
fun PreviewOverview() {
    val book = Book(
        title = "Boats Boats Boats Boats Boats",
        author = "Sarah Andersen",
        year = 2021,
        id = 0,
        status = "Unread",
        genre = "Something",
        isbn = "901908000"
    )
    OverviewContent(
        book = book,
        onDeleteBook = {},
        onBackPress = { false },
        modifier = Modifier,
        onUpdateBook = {}
    )
}