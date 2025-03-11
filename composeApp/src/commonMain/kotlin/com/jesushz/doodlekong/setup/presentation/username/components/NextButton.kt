package com.jesushz.doodlekong.setup.presentation.username.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onButtonClicked,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            backgroundColor = Color.Transparent
        ),
        border = ButtonDefaults.outlinedBorder.copy(
            width = 2.dp,
            brush = SolidColor(Color.Black)
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp
        )
    }
}