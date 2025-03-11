package com.jesushz.doodlekong.setup.presentation.select_room

import com.jesushz.doodlekong.core.data.network.ws.Room

data class SelectRoomState(
    val username: String = "",
    val searchRoom: String = "",
    val rooms: List<Room> = emptyList(),
)
