package com.mukund.bookcompanion.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomHomeFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add, contentDescription = "Addition button")
        Text(
            text = "new".uppercase(),
        )
    }
}