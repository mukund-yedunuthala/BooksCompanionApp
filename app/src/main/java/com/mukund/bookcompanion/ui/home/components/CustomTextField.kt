package com.mukund.bookcompanion.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalMaterial3Api
@Composable
fun CustomAdditionTextField(
    modifier: Modifier,
    text : String,
    placeholder: String,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    ) {
    OutlinedTextField(
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange =  onChange,
        leadingIcon = leadingIcon,
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = imeAction,
            keyboardType = keyboardType,
            autoCorrect = false
        ),
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(20.dp),
        placeholder = {
            Text(text = placeholder,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.LightGray
                )
            )
        }

    )
}