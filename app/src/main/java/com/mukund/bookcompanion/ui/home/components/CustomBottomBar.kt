package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.home.BooksViewModel
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors


// ── Bottom bar ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@ExperimentalMaterial3Api
@Composable
fun CustomBottomBar(
    viewModel: BooksViewModel,
) {
    val haptic = LocalHapticFeedback.current
    var showSheet by remember { mutableStateOf(false) }
    val borderColor = bookColors.rule

    BottomAppBar(
        containerColor = bookColors.paper,
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 0.dp),
        modifier = Modifier
            .height(76.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .drawBehind {
                // Hairline top border replacing Material's default elevation shadow
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 0.5.dp.toPx()
                )
            },
        actions = {
            // Wordmark — left-anchored, replaces filter buttons
            Text(
                text = "Library",
                style = AppType.labelMicroMono,
                color = bookColors.inkFaint,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    showSheet = true
                },
                containerColor = bookColors.ink,
                contentColor = bookColors.paper,
                shape = RoundedCornerShape(999.dp),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                text = {
                    Text(
                        text = "New entry",
                        style = AppType.fabLabelSerif,
                    )
                }
            )
        }
    )

    if (showSheet) {
        BookAdditionBottomSheet(
            onDismiss = { showSheet = false },
            addBook = { book -> viewModel.addBook(book) },
            books = viewModel.books,
            updateBook = { },
        )
    }
}