package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_GAME_ERROR

data class GameError(
    val errorType: Int
): BaseModel(TYPE_GAME_ERROR) {

    companion object {

        const val ERROR_ROOM_NOT_FOUND = 0
    }
}
