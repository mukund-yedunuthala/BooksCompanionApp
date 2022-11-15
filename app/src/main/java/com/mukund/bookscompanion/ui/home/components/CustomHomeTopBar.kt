package com.mukund.bookscompanion.ui.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@ExperimentalMaterial3Api
@Composable
fun CustomHomeTopBar() {
    LargeTopAppBar(title = {
        Text(text = "Books Companion")
    })
}