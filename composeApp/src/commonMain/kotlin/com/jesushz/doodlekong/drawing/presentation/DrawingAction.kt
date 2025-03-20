package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

sealed interface DrawingAction {

    data class OnNewPathStart(val offset: Offset): DrawingAction
    data class OnDrag(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data class OnSelectedColor(val color: Color): DrawingAction
    data object OnClearCanvasClick: DrawingAction
    data class OnMessageChanged(val message: String): DrawingAction
    data object OnSendClick: DrawingAction
    data object OnClearClick: DrawingAction
    data object OnUndo: DrawingAction
    data class OnNewWordSelected(val word: String): DrawingAction
    data object OnPlayersClick: DrawingAction
    data object OnStartRecording: DrawingAction

}
