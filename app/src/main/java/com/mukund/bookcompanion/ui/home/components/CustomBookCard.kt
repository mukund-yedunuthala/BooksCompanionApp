package com.mukund.bookcompanion.ui.home.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.core.Constants.Companion.NO_VALUE
import com.mukund.bookcompanion.design.CormorantGaramond
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.design.JetBrainsMono
import com.mukund.bookcompanion.domain.model.Book
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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickable { navigateTo(book.id) }
                .padding(horizontal = 28.dp, vertical = 22.dp)
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
                        fontFamily = JetBrainsMono,
                        fontSize = 9.sp,
                        letterSpacing = 0.14.sp,
                        color = bookColors.inkFaint,
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
                            .size(7.dp)
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
                        fontFamily = JetBrainsMono,
                        fontSize = 9.sp,
                        letterSpacing = 0.12.sp,
                        color = bookColors.inkSoft,
                    )
                }
            }

            // ── Title ─────────────────────────────────────────────
            Text(
                text = book.title.trim(),
                fontFamily = CormorantGaramond,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 27.sp,
                letterSpacing = (-0.01).sp,
                color = bookColors.ink,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // ── Author ────────────────────────────────────────────
            Text(
                text = "by ${book.author.trim()}",
                fontFamily = CormorantGaramond,
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
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
                    fontFamily = IBMPlexSans,
                    fontSize = 11.sp,
                    letterSpacing = 0.02.sp,
                    color = bookColors.inkFaint,
                )
            }
        }

        HorizontalDivider(
            color = bookColors.ruleSoft,
            thickness = 0.5.dp,
        )
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