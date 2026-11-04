package com.mukund.bookcompanion.ui.overview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.settings.components.BackNavRow
import com.mukund.bookcompanion.ui.theme.bookColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewTopBar(
    onBackPress: () -> Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = bookColors.paper,
            scrolledContainerColor = bookColors.paper,
        ),
        navigationIcon = {
            com.mukund.bookcompanion.ui.settings.components.BackNavRow(
                text = stringResource(R.string.home_library),
                description = stringResource(R.string.back),
                onClick = { onBackPress() },
            )
        },
        title = {},
        scrollBehavior = scrollBehavior
    )
}
