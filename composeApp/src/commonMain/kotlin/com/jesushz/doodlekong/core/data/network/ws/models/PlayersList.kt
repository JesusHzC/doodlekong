package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PLAYERS_LIST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayersList(
    val players: List<PlayerData>,
    override val type: String = TYPE_PLAYERS_LIST
): BaseModel()
