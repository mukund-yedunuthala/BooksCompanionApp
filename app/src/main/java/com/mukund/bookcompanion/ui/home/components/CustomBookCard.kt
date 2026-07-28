package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.design.BookCompanionBorders
import com.mukund.bookcompanion.design.BookCompanionSpacing
import com.mukund.bookcompanion.domain.model.Book
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme
import com.mukund.bookcompanion.ui.theme.bookColors


@Composable
fun CustomBookCardNew(
    book: Book,
    navigateTo: (bookId: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int,
    visibleState: MutableTransitionState<Boolean>,
) {
    // Cap the per-item entrance stagger so long lists don't take seconds to settle.
    val staggerDelay = minOf(index, 6) * 60

    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 300, delayMillis = staggerDelay)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300, delayMillis = staggerDelay)
        )
    ) {
        // Single column so the hairline divider sits *below* the card. (Previously the divider
        // was a second child of AnimatedVisibility, which stacks children like a Box and drew the
        // rule on top of the card's top edge.)
        Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button) { navigateTo(book.id) }
                .padding(horizontal = BookCompanionSpacing.gutter, vertical = 22.dp)
        ) {

            // ── Genre + status row ────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Genre — only shown if available
                if (book.genre != NO_VALUE) {
                    Text(
                        text = book.genre.trim().uppercase(),
                        style = AppType.labelMicroMono,
                        color = bookColors.inkFaint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                } else {
                    Spacer(Modifier.width(0.dp))
                }

                // Status dot + label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val dotColor = when (book.status.lowercase()) {
                        "read"    -> bookColors.sage
                        "reading" -> bookColors.ochre
                        else      -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .size(BookCompanionSpacing.statusDot)
                            .border(
                                width = 1.dp,
                                color = if (book.status.lowercase() == "unread")
                                    bookColors.inkFaint
                                else Color.Transparent,
                                shape = CircleShape
                            )
                            .background(dotColor, CircleShape)
                    )
                    Text(
                        text = book.status.trim(),
                        style = AppType.labelMicroMono,
                        color = bookColors.inkSoft,
                    )
                }
            }

            // ── Title ─────────────────────────────────────────────
            Text(
                text = book.title.trim(),
                style = AppType.titleSerif,
                color = bookColors.ink,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // ── Author ────────────────────────────────────────────
            Text(
                text = stringResource(R.string.overview_by_author, book.author.trim()),
                style = AppType.authorSerifItalic,
                color = bookColors.inkSoft,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // ── Metadata line — year and pages if available ───────
            val metaParts = buildList {
                if (book.year != 0L) add(book.year.toString())
                // TODO: add book.pages here once available in Book model
            }
            if (metaParts.isNotEmpty()) {
                Text(
                    text = metaParts.joinToString(" · "),
                    style = AppType.caption,
                    color = bookColors.inkFaint,
                )
            }
        }

        HorizontalDivider(
            color = bookColors.ruleSoft,
            thickness = BookCompanionBorders.hairline,
        )
        }
    }
}

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

    BooksCompanionTheme(darkTheme = true) {
        CustomBookCardNew(
            book = sampleBook,
            navigateTo = { bookId -> println("Navigating to $bookId") },
            index = 0,
            visibleState = visibleState,
            modifier = Modifier.padding(16.dp)
        )
    }
}