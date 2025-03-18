package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawingState(
    val username: String = "",
    val roomName: String = "",
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList()
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>
)
