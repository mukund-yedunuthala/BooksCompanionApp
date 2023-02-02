package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@ExperimentalMaterial3Api
@Composable
fun CustomHomeTopBar(settings: () -> Unit): Int {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("All", "Read", "Unread")
    Column {
        LargeTopAppBar(
                title = {
                    Text(
                        text = "Books Companion",
                        fontWeight = FontWeight.W400,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    IconButton(onClick = { settings() }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings")
                    }
                }
            )
        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
    }
    return state
}