package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.ui.home.BooksViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@ExperimentalMaterial3Api
@Composable
fun CustomBottomBar(
    viewModel: BooksViewModel,
    categories: Array<BookCategory>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>,
) {
    val haptic = LocalHapticFeedback.current
    var showSheet by remember { mutableStateOf(false) }

    BottomAppBar(
        actions = {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 15.dp)
            ) {
                categories.forEach { category ->
                    ToggleButton(
                        checked = currentCategory == category,
                        onCheckedChange = { selected ->
                            if (selected && category != currentCategory) {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                setCurrentCategory(category)
                                visibleStateAll.targetState = false
                                visibleStateRead.targetState = false
                                visibleStateUnread.targetState = false
                            }
                        },
                        modifier = Modifier.semantics { role = Role.RadioButton }
                    ) {
                        Icon(
                            painter = painterResource(category.icon),
                            contentDescription = category.name,
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                        Text(
                            text = category.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    showSheet = true
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add Book",
                    modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize)
                )
            }
        }
    )

    if (showSheet) {
        BookAdditionBottomSheet(
            onDismiss = { showSheet = false },
            addBook = { book -> viewModel.addBook(book) },
            books = viewModel.books,
        )
    }
}
