package com.mukund.bookcompanion.ui.overview.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewTopBar(backPress: () -> Boolean, navigateTo: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Overview") },
        navigationIcon = {
            IconButton(onClick = { backPress.invoke() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Return to home"
                )
            }
        },
        actions = {
            IconButton(onClick = { navigateTo.invoke() }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    )
}