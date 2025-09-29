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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.domain.model.Book

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                            .fillMaxWidth(0.9f)
                            .height(30.dp),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier.weight(0.1f),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(onClick = { expandedCard = !expandedCard }) {
                            Icon(
                                painter = painterResource(id = R.drawable.expand_circle_down),
                                contentDescription = "Expand",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .height(30.dp)
                                    .size(IconButtonDefaults.smallIconSize)
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = book.author.trim(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                if (expandedCard) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        CustomSuggestionChip( text = book.year.toString().trim())
                        CustomSuggestionChip( text = "Status: ${book.status.trim()}")
                        if (book.genre != NO_VALUE) CustomSuggestionChip( text = "Genre: ${book.genre}")
                    }


                }
            }
        }
    }
}

@Composable
private fun CustomSuggestionChip(text: String) {
    SuggestionChip(
        onClick = { },
        label = {
            Text(
                text = text,
            )
        },
        modifier = Modifier
            .wrapContentWidth()
            .padding(10.dp),
        shape = ButtonDefaults.shape
    )
}