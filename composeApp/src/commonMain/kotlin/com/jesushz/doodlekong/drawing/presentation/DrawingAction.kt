package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

sealed interface DrawingAction {

    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data class OnSelectedColor(val color: Color): DrawingAction
    data object OnClearCanvasClick: DrawingAction

}