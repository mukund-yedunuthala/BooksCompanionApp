package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book

@ExperimentalMaterial3Api
@Composable
fun CustomBookCard(
    book: Book,
    navigateTo: (bookId: Int) -> Unit,
    modifier: Modifier,
    index: Int,
    visibleState: MutableTransitionState<Boolean>,
) {
    var expandedCard by remember { mutableStateOf(false) }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally (
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
        )
    ) {
        Card(
            modifier = modifier
                .padding(16.dp, 8.dp)
                .animateContentSize(
                    animationSpec = spring(dampingRatio = 1f)
                ),
            shape = RoundedCornerShape(10.dp),
            onClick = { navigateTo.invoke(book.id) }
        ) {
            Column() {
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
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Year of publication: ${book.year.toString()}",
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(10.dp),
                            textAlign = TextAlign.Start
                        )
                        if (book.genre != NO_VALUE) {
                            Text(
                                text = "|",
                                fontStyle = FontStyle.Italic,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(10.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Genre: ${book.genre}",
                                fontStyle = FontStyle.Italic,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(10.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

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
}
