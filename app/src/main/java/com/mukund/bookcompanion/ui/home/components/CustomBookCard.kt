package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.domain.model.Book

@ExperimentalMaterial3Api
@Composable
fun CustomBookCard(
    book: Book,
    navigateTo: (bookId: Int) -> Unit,
    modifier: Modifier
) {
    var expandedCard by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .animateContentSize(
                animationSpec = spring(dampingRatio = 1f)
            )
        ,
        shape = RoundedCornerShape(10.dp),
        onClick = { navigateTo.invoke(book.id) }
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
                    SuggestionChip(
                        onClick = {},
                        label = { Text("Status: ${book.status.trim()}") },
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}
