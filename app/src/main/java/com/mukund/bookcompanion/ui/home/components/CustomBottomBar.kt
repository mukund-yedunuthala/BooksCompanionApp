package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CustomBottomBar(onFABClick: () -> Unit, haptic: HapticFeedback): Int {
    var state by remember { mutableStateOf(0) }
    BottomAppBar(
        actions = {
            TextButton(
                onClick = {
                    state = 0
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List All",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Text(
                    text = "All",
                    modifier = Modifier.padding(5.dp)
                )
            }
            TextButton(onClick = {
                state = 1
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Read Books",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Text(
                    text = "Read",
                    modifier = Modifier.padding(5.dp)
                )

            }
            TextButton(onClick ={
                state = 2
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }) {Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Unread Books",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Text(
                    text = "Unread",
                    modifier = Modifier.padding(5.dp)
                )

            }
        },
        floatingActionButton = {
            CustomHomeFab (
                onClick = onFABClick
            )
        }
    )
    return state
}