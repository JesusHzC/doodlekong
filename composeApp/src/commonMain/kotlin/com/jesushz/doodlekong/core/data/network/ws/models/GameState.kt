package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_GAME_STATE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val drawingPlayer: String,
    val word: String,
    override val type: String = TYPE_GAME_STATE
): BaseModel()
