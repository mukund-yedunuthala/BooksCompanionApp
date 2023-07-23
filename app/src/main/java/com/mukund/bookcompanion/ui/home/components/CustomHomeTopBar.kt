package com.mukund.bookcompanion.ui.home.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.settings.mToast

@ExperimentalMaterial3Api
@Composable
fun CustomHomeTopBar(
    settings: () -> Unit,
    haptic: HapticFeedback,
    onSortSelected: (String) -> Unit,
    selectedIndex: Int,
    onRadioSelected: (Int) -> Unit,) {
    val sortingOptions = listOf("Title", "Year")
    var expanded by remember { mutableStateOf(false) }
    Column {
        LargeTopAppBar(
            title = {
                Text(
                    text = "Book Companion",
                    fontWeight = FontWeight.W400,
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            actions = {
                IconButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    expanded = true
//                    mToast(context, "Coming soon!")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort),
                        contentDescription = "Sort",
                        modifier = Modifier.size(25.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset((10).dp,0.dp)
                ) {
                    sortingOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onSortSelected(option)
                            },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = {onRadioSelected(index)})
                                ) {
                                    RadioButton(
                                        selected = index == selectedIndex,
                                        onClick = { onRadioSelected(index) }
                                    )
                                    Text(text = option)
                                }
                            },
                            contentPadding = PaddingValues(5.dp)
                        )
                    }
                }
                IconButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    settings.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun CustomLandscapeHomeTopBar(
    settings: () -> Unit,
    haptic: HapticFeedback,
    context: Context,
): Unit {
    //var scopeState by remember { mutableStateOf(0) }
    //val titles = listOf("All", "Read", "Unread")
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Book Companion",
                    fontWeight = FontWeight.W400,
                )
            },
            actions = {
                IconButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    mToast(context, "Coming soon!")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort),
                        contentDescription = "Sort",
                        modifier = Modifier.size(25.dp)
                    )
                }
                IconButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    settings.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        )
    }
}