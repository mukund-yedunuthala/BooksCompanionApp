package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.ui.home.BookCategory

@Composable
fun CustomNavigationRail(
    currentCategory: BookCategory,
    setCurrentCategory: (BookCategory) -> Unit,
    onFabClick: () -> Unit,
) {
    NavigationRail(
        modifier = Modifier.width(72.dp),
        content = {
            Spacer(modifier = Modifier.height(60.dp)) // Empty space above the NavigationRailItems
            val categories = BookCategory.values()
            categories.forEach { category ->
                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name
                        )
                    },
                    selected = category == currentCategory,
                    onClick = {
                        setCurrentCategory(category)
                    }
                )
            }
            FloatingActionButton(
                onClick = onFabClick,
                content = { Icon(Icons.Filled.Add, contentDescription = "Add Book") }
            )
        }
    )
}