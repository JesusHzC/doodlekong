package com.jesushz.doodlekong.drawing.data.mappers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.jesushz.doodlekong.core.data.network.ws.models.DrawData
import com.jesushz.doodlekong.drawing.presentation.PathData
import com.jesushz.doodlekong.util.getSystemTime

fun DrawData.toPathData(): PathData {
    return PathData(
        id = getSystemTime().toString(),
        color = Color(color),
        path = listOf(Offset(fromX, fromY), Offset(toX, toY))
    )
}
