@file:OptIn(ExperimentalSerializationApi::class)

package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_JOIN_ROOM_HANDSHAKE
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String,
    override val type: String = TYPE_JOIN_ROOM_HANDSHAKE
): BaseModel()
