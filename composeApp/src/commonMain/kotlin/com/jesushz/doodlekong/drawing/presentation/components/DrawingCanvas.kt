package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    paths: List<PathData>,
    onDraw: (DrawData) -> Unit
) {
    val currentPath = remember { mutableStateOf<Path?>(null) }
    val currentColor = remember { mutableStateOf(Color.Black) }
    val currentThickness = remember { mutableStateOf(5f) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val path = Path().apply { moveTo(offset.x, offset.y) }
                        currentPath.value = path
                        onDraw(DrawData(currentColor.value, currentThickness.value, path))
                    },
                    onDrag = { change, _ ->
                        currentPath.value?.lineTo(change.position.x, change.position.y)
                    },
                    onDragEnd = {
                        currentPath.value?.let { path ->
                            onDraw(DrawData(currentColor.value, currentThickness.value, path))
                        }
                        currentPath.value = null
                    }
                )
            }
    ) {
        paths.forEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                style = Stroke(width = pathData.thickness, cap = StrokeCap.Round)
            )
        }
    }
}

data class PathData(val path: Path, val color: Color, val thickness: Float)

data class DrawData(val color: Color, val thickness: Float, val path: Path)

