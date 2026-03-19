package com.mukund.bookcompanion.ui.overview.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mukund.bookcompanion.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverviewTopBar(
    onBackPress: () -> Boolean,
    onNavigateTo: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.overview_title)) },
        navigationIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(stringResource(R.string.back)) } },
                state = rememberTooltipState()
            ) {
                IconButton(
                    onClick = { onBackPress() },
                    modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                    shapes = IconButtonDefaults.shapes()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = null, // described by tooltip
                        modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                    )
                }
            }
        },
        actions = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(stringResource(R.string.edit)) } },
                state = rememberTooltipState()
            ) {
                IconButton(
                    onClick = { onNavigateTo() },
                    modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
                    shapes = IconButtonDefaults.shapes()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null, // described by tooltip
                        modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
