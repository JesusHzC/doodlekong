package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastForEach
import com.jesushz.doodlekong.drawing.presentation.PathData
import kotlin.math.abs

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    paths: List<PathData>,
    dragIsAvailable: Boolean = true,
    currentPath: PathData?,
    onDragStart: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDrag: (Offset) -> Unit
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(Color.White)
            .pointerInput(dragIsAvailable) {
                detectDragGestures(
                    onDragStart = {
                        onDragStart(it)
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDrag = { change, _ ->
                        onDrag(change.position)
                    },
                    onDragCancel = {
                        onDragEnd()
                    }
                )
            }
    ) {
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color
            )
        }
        currentPath?.let {
            drawPath(
                path = it.path,
                color = it.color
            )
        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 5
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)

                if (dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }
    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
