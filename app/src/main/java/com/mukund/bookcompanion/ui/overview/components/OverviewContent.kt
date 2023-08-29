package com.mukund.bookcompanion.ui.overview.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.domain.model.Book

@Composable
fun OverviewContent(
    book: Book,
    navigateTo: () -> Unit,
    deleteBook: (book: Book) -> Unit,
    backPress: () -> Boolean
) {
    val showDeleteDialog = remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        OutlinedCard(
            modifier = Modifier
                .padding(20.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(10.dp, top = 5.dp, bottom = 5.dp)

                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(10.dp, top = 5.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.W400
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = book.year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(10.dp, top = 5.dp, bottom = 5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W300
                    )
                    SuggestionChip(
                        onClick = {  },
                        modifier = Modifier
                            .padding(10.dp, top = 5.dp, bottom = 5.dp)
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        label = {
                            Text(text = book.status.toString().uppercase())
                        },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomEntryChipButton(
                            onClick = { navigateTo.invoke() },
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit button",
                            leadText = "Edit",
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        CustomEntryChipButton(
                            onClick = {showDeleteDialog.value = true},
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete button",
                            leadText = "Delete",
                        )
                    }

                }
            }
        }
    }
    if (showDeleteDialog.value) {
        OverviewConfirmDelete(
            showDeleteDialog,
            deleteBook,
            book,
            backPress
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
        status = "Unread"
    )
    OverviewContent(book = book, navigateTo = { }, deleteBook = {
    }, backPress = { false })
}