package com.mukund.bookcompanion.ui.overview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverviewContent(
    modifier: Modifier = Modifier,
    book: Book,
    onNavigateTo: () -> Unit,
    onDeleteBook: (Book) -> Unit,
    onBackPress: () -> Boolean
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = book.year.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
                if (book.genre != NO_VALUE) {
                    LabeledDetailRow(
                        label = stringResource(R.string.genre_label),
                        value = book.genre
                    )
                }
                if (book.isbn != NO_VALUE) {
                    LabeledDetailRow(
                        label = stringResource(R.string.isbn_label),
                        value = book.isbn
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        ElevatedFilterChip(
            selected = false,
            onClick = {},
            label = { Text(book.status.uppercase()) },
            enabled = false // display-only, not interactive
        )

        Spacer(Modifier.height(24.dp))
        val editLabel = stringResource(R.string.edit)
        val deleteLabel = stringResource(R.string.delete)
        ButtonGroup(
            overflowIndicator = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            toggleableItem(
                weight = 1f,
                checked = false,
                onCheckedChange = { onNavigateTo() },
                label = editLabel,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null,
                        modifier = Modifier.size(IconButtonDefaults.smallIconSize)
                    )
                }
            )
            toggleableItem(
                weight = 1f,
                checked = false,
                onCheckedChange = { showDeleteDialog = true },
                label = deleteLabel,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = null,
                        modifier = Modifier.size(IconButtonDefaults.smallIconSize)
                    )
                }
            )
        }
    }

    if (showDeleteDialog) {
        OverviewConfirmDelete(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                onDeleteBook(book)
                onBackPress()
            },
            book = book
        )
    }
}

@Composable
private fun LabeledDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
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
    OverviewContent(book = book, onNavigateTo = { }, onDeleteBook = {
    }, onBackPress = { false }, modifier = Modifier)
}