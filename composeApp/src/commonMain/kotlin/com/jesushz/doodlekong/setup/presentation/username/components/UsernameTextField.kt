package com.jesushz.doodlekong.setup.presentation.username.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun UsernameTextField(
    modifier: Modifier = Modifier,
    username: String,
    label: String,
    onUsernameChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = {
            Text(
                text = label,
                color = Color.Black,
                fontSize = if (isFocused) 12.sp else 18.sp
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = 18.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
            .onFocusChanged {
                isFocused = it.isFocused
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
        )
    )
}
