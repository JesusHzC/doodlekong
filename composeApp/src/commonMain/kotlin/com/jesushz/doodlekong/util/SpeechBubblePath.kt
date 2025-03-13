package com.jesushz.doodlekong.util

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path

fun pathIncoming(
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

fun pathOutgoing(
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