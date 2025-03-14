package com.jesushz.doodlekong.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.speechBubble(
    type: BubbleType = BubbleType.INCOMING,
    cornerRadius: Dp = 10.dp,
    tipSize: Dp = 10.dp,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 4.dp
) = drawWithContent {
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val tipSizePx = with(density) { tipSize.toPx() }

    val path = when (type) {
        BubbleType.INCOMING -> pathIncoming(tipSizePx, cornerRadiusPx, size)
        BubbleType.OUTGOING -> pathOutgoing(tipSizePx, cornerRadiusPx, size)
    }

    drawPath(
        path = path,
        color = borderColor,
        style = Stroke(
            width = borderWidth.toPx()
        )
    )
    drawContent()
}
