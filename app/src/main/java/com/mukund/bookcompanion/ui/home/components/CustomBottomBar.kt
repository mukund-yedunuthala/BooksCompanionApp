package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.mukund.bookcompanion.ui.home.BookCategory

@ExperimentalMaterial3Api
@Composable
fun CustomBottomBar(
    onFABClick: () -> Unit,
    haptic: HapticFeedback,
    categories: Array<BookCategory>,
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    visibleStateAll: MutableTransitionState<Boolean>,
    visibleStateRead: MutableTransitionState<Boolean>,
    visibleStateUnread: MutableTransitionState<Boolean>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BottomAppBar(
            actions = {
                categories.forEach { category ->
                    val selected = currentCategory == category
                    TextButton(onClick = {
                        if (category != currentCategory) {
                            setCurrentCategory(category)
                            visibleStateAll.targetState = false
                            visibleStateRead.targetState = false
                            visibleStateUnread.targetState = false
                        }
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            tint = if (category == currentCategory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onFABClick() },
                    content = { Icon(Icons.Filled.Add, contentDescription = "Add Book") }
                )
            }
        )
    }
}