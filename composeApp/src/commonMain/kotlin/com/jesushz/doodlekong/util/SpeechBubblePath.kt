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
        moveTo(
            x = 0f,
            y = 0f
        )
        lineTo(
            x = size.width - cornerRadius,
            y = 0f
        )
        quadraticTo(
            x1 = size.width,
            y1 = 0f,
            x2 = size.width,
            y2 = cornerRadius
        )
        lineTo(
            x = size.width,
            y = size.height - cornerRadius
        )
        quadraticTo(
            x1 = size.width,
            y1 = size.height,
            x2 = size.width - cornerRadius,
            y2 = size.height
        )
        lineTo(
            x = tipSize + cornerRadius,
            y = size.height
        )
        quadraticTo(
            x1 = tipSize,
            y1 = size.height,
            x2 = tipSize,
            y2 = size.height - cornerRadius
        )
        lineTo(
            x = tipSize,
            y = cornerRadius
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
        moveTo(
            x = size.width,
            y = 0f
        )
        lineTo(
            x = 0f + cornerRadius,
            y = 0f
        )
        quadraticTo(
            x1 = 0f,
            y1 = 0f,
            x2 = 0f,
            y2 = cornerRadius
        )
        lineTo(
            x = 0f,
            y = size.height - cornerRadius
        )
        quadraticTo(
            x1 = 0f,
            y1 = size.height,
            x2 = cornerRadius,
            y2 = size.height
        )
        lineTo(
            x = size.width - cornerRadius - tipSize,
            y = size.height
        )
        quadraticTo(
            x1 = size.width - tipSize,
            y1 = size.height,
            x2 = size.width - tipSize,
            y2 = size.height - cornerRadius
        )
        lineTo(
            x = size.width - tipSize,
            y = cornerRadius
        )
        close()
    }
}

enum class BubbleType {
    INCOMING,
    OUTGOING
}