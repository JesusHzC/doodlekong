package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_DATA
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DrawData(
    val roomName: String,
    val color: Int,
    val thickness: Float,
    val fromX: Float,
    val fromY: Float,
    val toX: Float,
    val toY: Float,
    val motionEvent: Int,
    override val type: String = TYPE_DRAW_DATA
): BaseModel()
