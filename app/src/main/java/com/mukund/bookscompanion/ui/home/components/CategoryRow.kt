package com.mukund.bookscompanion.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryRow(
    current: String? = "Unread",
): String? {
    val categories = listOf<String>("Unread", "Read")
    val (selectedCat, onCatSelected) = remember {
        mutableStateOf(current)
    }
    //if (!current.isNullOrEmpty()) { selectedCat = current }
    Row(
        Modifier
            .selectableGroup()
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Status:",
            fontSize = 18.sp,
            modifier = Modifier.padding(20.dp)
        )
        categories.forEach { text ->
            Column(
                Modifier
                    .padding(20.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .selectable(
                            selected = (text == selectedCat),
                            onClick = { onCatSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedCat),
                        onClick = null
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

            }
        }
    }
    return selectedCat
}
