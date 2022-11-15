package com.mukund.bookscompanion.ui.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mukund.bookscompanion.domain.model.Book

@ExperimentalMaterial3Api
@Composable
fun CustomBookCard(
    book: Book,
    deleteBook: (book: Book) -> Unit,
    navigateTo: (bookId: Int) -> Unit
    ) {
    var expandedCard by remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(10.dp),
        onClick = { expandedCard = !expandedCard }
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = book.title.trim(),
                    modifier = Modifier
                        .padding(10.dp, top = 10.dp)
                        .fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier.weight(0.1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { expandedCard = !expandedCard }) {
                        Icon(
                            Icons.Default.MoreVert,
                            "Expand",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            Text(
                text = book.author.trim(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(10.dp)
            )
            if (expandedCard) {
                Text(
                    text = book.year.toString(),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    AssistChip(
                        onClick = { showDeleteDialog.value = !showDeleteDialog.value },
                        label = { Text("Delete".uppercase()) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete this book",
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(10.dp)
                    )
                    AssistChip(
                        onClick = { navigateTo(book.id) },
                        label = { Text("Edit".uppercase()) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit this book",
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(10.dp)
                    )
                }
            }
        }
    }
    if (showDeleteDialog.value) {
        ConfirmDelete(
            showDeleteDialog,
            deleteBook,
            book
        )
    }
}

@Composable
fun ConfirmDelete(
    showDeleteDialog: MutableState<Boolean>,
    deleteBook: (book:Book) -> Unit,
    book: Book
) {
    AlertDialog(
        onDismissRequest = { showDeleteDialog.value = false },
        title = {
            Text("Remove?")
        },
        text = {
            Text("Book will be deleted")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    showDeleteDialog.value = false
                    deleteBook(book)
                }
            ) {
                Text("Confirm".uppercase())
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDeleteDialog.value = false
                }
            ) {
                Text("Dismiss".uppercase())
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}