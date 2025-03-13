package com.jesushz.doodlekong.util

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class SpeechBubbleShape(
    private val cornerRadius: Dp = 10.dp,
    private val tipSize: Dp = 10.dp,
    private val bubbleType: BubbleType = BubbleType.INCOMING
): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tipSize = with(density) { tipSize.toPx() }
        val cornerRadius = with(density) { cornerRadius.toPx() }
        val path = when (bubbleType) {
            BubbleType.INCOMING -> pathIncoming(tipSize, cornerRadius, size)
            BubbleType.OUTGOING -> pathOutgoing(tipSize, cornerRadius, size)
        }

        return Outline.Generic(path)
    }
}
