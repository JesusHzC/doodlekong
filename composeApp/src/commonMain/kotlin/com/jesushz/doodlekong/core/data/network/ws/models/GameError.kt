package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_GAME_ERROR
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameError(
    val errorType: Int,
    override val type: String = TYPE_GAME_ERROR
): BaseModel() {

    companion object {

        const val ERROR_ROOM_NOT_FOUND = 0
    }
}
