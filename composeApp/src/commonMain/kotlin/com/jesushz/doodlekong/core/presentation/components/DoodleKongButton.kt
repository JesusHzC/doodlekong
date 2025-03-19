package com.jesushz.doodlekong.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DoodleKongButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    isSelected: Boolean = false,
    onButtonClicked: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onButtonClicked,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isSelected) Color.White else Color.Black,
            backgroundColor = if (isSelected) Color.Black else Color.Transparent,
            disabledContentColor = if (isSelected) Color.White else Color.Black,
        ),
        border = ButtonDefaults.outlinedBorder.copy(
            width = 2.dp,
            brush = SolidColor(Color.Black)
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                fontSize = 24.sp
            )
        }
    }
}