package com.mukund.bookcompanion.ui.overview.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverviewTopBar(backPress: () -> Boolean, navigateTo: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Overview") },
        navigationIcon = {
            IconButton(onClick = { backPress.invoke() }) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    "Return to home",
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        },
        actions = {
            IconButton(onClick = { navigateTo.invoke() }) {
                Icon(
                    painterResource(id = R.drawable.edit),
                    contentDescription = "Edit",
                    modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                )
            }
        }
    )
}