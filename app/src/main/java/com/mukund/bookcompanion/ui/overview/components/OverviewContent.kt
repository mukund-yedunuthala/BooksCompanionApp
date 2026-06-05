package com.mukund.bookcompanion.ui.overview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.design.CormorantGaramond
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
            .padding(horizontal = 28.dp)
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
            text = "by ${book.author.trim()}",
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

        // ── Rating placeholder ────────────────────────────────
        // TODO: Render star row once Book model has a rating field
        //       Reference: 5-star row using bookColors.terracotta,
        //       only rendered if rating != null
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 28.dp)
        ) {
            repeat(5) {
                Icon(
                    painter = painterResource(R.drawable.star),
                    contentDescription = null,
                    tint = bookColors.rule,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // ── Divider ───────────────────────────────────────────
        HorizontalDivider(
            color = bookColors.rule,
            thickness = 0.5.dp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // ── Metadata rows ─────────────────────────────────────
        if (book.year != 0L) {
            MetaRow(label = "Year", value = book.year.toString())
        }
        // TODO: add MetaRow for pages once Book model supports it
        if (book.isbn != NO_VALUE) {
            MetaRow(label = "ISBN", value = book.isbn, mono = true)
        }
        // TODO: add MetaRow for dateAdded / dateFinished
        //       once Book model has those fields

        // ── Marginalia / Notes ────────────────────────────────
        // TODO: Render once Book model has a notes field
        //       Reference: serif italic blockquote with
        //       1.5dp left border in bookColors.rule

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
                    .clickable { showEditSheet = true }
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
                        text = "Edit",
                        style = AppType.labelMicroMono,
                        color = bookColors.paper,
                    )
                }
            }

            // Delete
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(width = 0.5.dp, color = bookColors.rule)
                    .clickable { showDeleteDialog = true }
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
                        text = "Delete",
                        style = AppType.labelMicroMono,
                        color = bookColors.inkSoft,
                    )
                }
            }
        }

        // ── Delete confirmation ───────────────────────────────
        if (showDeleteDialog) {
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = bookColors.terracotta.copy(alpha = 0.25f)
                    )
                    .background(bookColors.terracotta.copy(alpha = 0.05f))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Remove ")
                        withStyle(
                            SpanStyle(
                                fontFamily = CormorantGaramond,
                                fontStyle = FontStyle.Italic,
                                fontSize = 15.sp,
                            )
                        ) { append(book.title.trim()) }
                        append(" from your library?")
                    },
                    style = AppType.bodySmall,
                    color = bookColors.inkSoft,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(bookColors.terracotta)
                            .clickable {
                                onDeleteBook(book)
                                onBackPress()
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Confirm",
                            style = AppType.labelMicroMono,
                            color = bookColors.paper,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(width = 0.5.dp, color = bookColors.rule)
                            .clickable { showDeleteDialog = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            style = AppType.labelMicroMono,
                            color = bookColors.inkSoft,
                        )
                    }
                }
            }
        }
        if (showEditSheet) {
            BookAdditionBottomSheet(
                onDismiss = { showEditSheet = false },
                addBook = {},           // not used in edit mode
                updateBook = { book ->
                    onUpdateBook(book)
                    onBackPress()
                             },
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
            .border(width = 0.5.dp, color = borderColor)
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
    HorizontalDivider(color = bookColors.ruleSoft, thickness = 0.5.dp)
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