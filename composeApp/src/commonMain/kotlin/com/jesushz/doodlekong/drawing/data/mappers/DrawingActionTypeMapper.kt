package com.jesushz.doodlekong.drawing.data.mappers

import androidx.compose.ui.graphics.Color
import com.jesushz.doodlekong.drawing.data.models.DrawingActionType
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_baseline_undo_24
import doodlekong.composeapp.generated.resources.ic_eraser_checked
import doodlekong.composeapp.generated.resources.ic_eraser_unchecked
import org.jetbrains.compose.resources.DrawableResource

fun DrawingActionType.toColor(): Color {
    return when (this) {
        DrawingActionType.RED_COLOR -> Color.Red
        DrawingActionType.GREEN_COLOR -> Color.Green
        DrawingActionType.BLUE_COLOR -> Color.Blue
        DrawingActionType.ORANGE_COLOR -> Color(0xFFFFA726)
        DrawingActionType.YELLOW_COLOR -> Color.Yellow
        DrawingActionType.BLACK_COLOR -> Color.Black
        else -> Color.Transparent
    }
}

fun DrawingActionType.toDrawResource(isSelected: Boolean = false): DrawableResource? {
    return when (this) {
        DrawingActionType.UNDO -> Res.drawable.ic_baseline_undo_24
        DrawingActionType.ERASER -> {
            if (isSelected) {
                Res.drawable.ic_eraser_checked
            } else {
                Res.drawable.ic_eraser_unchecked
            }
        }
        else -> null
    }
}
