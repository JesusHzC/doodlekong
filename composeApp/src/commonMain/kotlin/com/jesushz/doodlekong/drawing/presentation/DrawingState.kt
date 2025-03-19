package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import com.jesushz.doodlekong.core.data.network.ws.models.DrawData
import com.jesushz.doodlekong.core.data.network.ws.models.PhaseChange
import com.jesushz.doodlekong.core.data.network.ws.models.PlayerData

data class DrawingState(
    val username: String = "",
    val roomName: String = "",
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val isUserDrawing: Boolean = false,
    val phase: PhaseChange = PhaseChange(null, 0L, null),
    val newWords: List<String> = emptyList(),
    val wordSelected: String? = null,
    val players: List<PlayerData> = emptyList(),
    val chatObjects: List<BaseModel> = emptyList(),
    val cutWord: String? = null,
    val showConnectionProgress: Boolean = true,
    val showChooseWordScreen: Boolean = false,
    val maxProgressBar: Long = 0L,
    val progressBarValue: Long = 0L,
    val message: String = "",
    val showDrawingActions: Boolean = false,
    val showSendChatMessage: Boolean = false,
    val showMic: Boolean = false,
    val canvasEnabled: Boolean = false
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>
)
