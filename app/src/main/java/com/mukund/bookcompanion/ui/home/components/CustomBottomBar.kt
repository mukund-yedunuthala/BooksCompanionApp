package com.mukund.bookcompanion.ui.home.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import com.mukund.bookcompanion.ui.home.BookCategory
import com.mukund.bookcompanion.R.drawable.add

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                            painter = painterResource(category.icon),
                            contentDescription = category.name,
                            tint = if (category == currentCategory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize)
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
                    content = { Icon(
                        painter = painterResource(id = add),
                        contentDescription = "Add Book"
                    ) }
                )
            }
        )
    }
}