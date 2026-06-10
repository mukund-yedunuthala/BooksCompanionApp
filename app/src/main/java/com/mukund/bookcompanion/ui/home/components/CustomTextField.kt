package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukund.bookcompanion.design.CormorantGaramond
import com.mukund.bookcompanion.design.IBMPlexSans
import com.mukund.bookcompanion.ui.theme.AppType
import com.mukund.bookcompanion.ui.theme.bookColors

// ── Editorial text field ──────────────────────────────────────────────────────
@Composable
fun EditorialTextField(
    value: String,
    label: String,
    placeholder: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    useSerif: Boolean = false,
    singleLine: Boolean = true,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(bottom = 18.dp)) {
        Text(
            text = label,
            style = AppType.labelMicroMono,
            color = bookColors.inkFaint,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(
                fontFamily = if (useSerif) CormorantGaramond else IBMPlexSans,
                fontSize = if (useSerif) 20.sp else 15.sp,
                fontWeight = if (useSerif) FontWeight.Medium else FontWeight.Normal,
                color = bookColors.ink,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            minLines = if (singleLine) 1 else 3,
            cursorBrush = SolidColor(bookColors.ink),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { inner ->
                Column {
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = if (useSerif) CormorantGaramond else IBMPlexSans,
                                fontSize = if (useSerif) 20.sp else 15.sp,
                                color = bookColors.inkFaint,
                            )
                        }
                        inner()
                    }
                    // Hairline underline — animates from rule to ink on focus
                    HorizontalDivider(
                        color = if (isFocused) bookColors.ink
                        else bookColors.rule,
                        thickness = 0.5.dp
                    )
                }
            }
        )
    }
}