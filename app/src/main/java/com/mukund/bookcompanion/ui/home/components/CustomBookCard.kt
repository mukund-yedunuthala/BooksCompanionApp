package com.mukund.bookcompanion.ui.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
fun CustomBookCard(
    book: Book,
    navigateTo: (bookId: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int,
    visibleState: MutableTransitionState<Boolean>,
) {
    var expandedCard by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
        )
    ) {
        OutlinedCard(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                ),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.elevatedCardElevation(),
            onClick = { navigateTo(book.id) }
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {

                // Title row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = book.title.trim(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        onClick = { expandedCard = !expandedCard },
                        modifier = Modifier.semantics {
                            stateDescription = if (expandedCard) "Collapse" else "Expand"
                        }
                    ) {
                        Icon(
                            // Use Material Icons instead of a custom painter resource
                            painter = if (expandedCard)
                                painterResource(id = R.drawable.expand_circle_down)
                            else
                                painterResource(id = R.drawable.expand_circle_down),
                            contentDescription = if (expandedCard) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize)
                        )
                    }
                }

                Text(
                    text = book.author.trim(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                if (expandedCard) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        BookChip(text = book.year.toString().trim())
                        BookChip(text = book.status.trim())
                        if (book.genre != NO_VALUE) BookChip(text = book.genre)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookChip(text: String) {
    SuggestionChip(
        onClick = {},
        label = { Text(text = text) },
        shape = SuggestionChipDefaults.shape,
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun PreviewCustomBookCard() {
    val sampleBook = Book(
        id = 1,
        title = "The Great Gatsby",
        author = "F. Scott Fitzgerald",
        year = 2025,
        status = "Read",
        genre = "Fiction",
        isbn = "8173507",
    )

    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    BooksCompanionTheme(darkTheme = false) {
        CustomBookCard(
            book = sampleBook,
            navigateTo = { bookId -> println("Navigating to $bookId") },
            index = 0,
            visibleState = visibleState,
            modifier = Modifier.padding(16.dp)
        )
    }
}