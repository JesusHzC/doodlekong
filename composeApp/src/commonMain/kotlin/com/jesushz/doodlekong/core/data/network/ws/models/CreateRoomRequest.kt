package com.jesushz.doodlekong.core.data.network.ws.models

data class CreateRoomRequest(
    val name: String,
    val maxPlayers: Int
)
