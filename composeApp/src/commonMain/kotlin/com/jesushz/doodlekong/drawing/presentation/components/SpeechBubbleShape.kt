package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
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

private fun pathIncoming(
    tipSize: Float,
    cornerRadius: Float,
    size: Size
): Path {
    return Path().apply {
        addRoundRect(
            RoundRect(
                left = tipSize,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = cornerRadius,
                radiusY = cornerRadius
            )
        )

        moveTo(
            x = 0f,
            y = 0f
        )

        lineTo(
            x = tipSize + cornerRadius,
            y = cornerRadius + tipSize
        )

        lineTo(
            x = tipSize + cornerRadius,
            y = 0f
        )

        close()
    }
}

private fun pathOutgoing(
    tipSize: Float,
    cornerRadius: Float,
    size: Size
): Path {
    return Path().apply {
        addRoundRect(
            RoundRect(
                left = 0f,
                top = 0f,
                right = size.width - tipSize,
                bottom = size.height,
                radiusX = cornerRadius,
                radiusY = cornerRadius
            )
        )

        moveTo(
            x = size.width,
            y = 0f
        )

        lineTo(
            x = size.width - tipSize - cornerRadius,
            y = 0f
        )

        lineTo(
            x = size.width - tipSize - cornerRadius,
            y = cornerRadius + tipSize
        )

        close()
    }
}

enum class BubbleType {
    INCOMING,
    OUTGOING
}
