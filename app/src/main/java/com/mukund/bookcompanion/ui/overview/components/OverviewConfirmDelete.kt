package com.mukund.bookcompanion.ui.overview.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.mukund.bookcompanion.domain.model.Book

@Composable
fun OverviewConfirmDelete(
    showDeleteDialog: MutableState<Boolean>,
    deleteBook: (book: Book) -> Unit,
    book: Book,
    backPress: () -> Boolean
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
                    backPress.invoke()
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