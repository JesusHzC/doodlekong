package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_JOIN_ROOM_HANDSHAKE

data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String
): BaseModel(TYPE_JOIN_ROOM_HANDSHAKE)
