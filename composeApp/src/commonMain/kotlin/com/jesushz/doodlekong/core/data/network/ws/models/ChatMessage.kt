package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CHAT_MESSAGE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val from: String,
    val roomName: String,
    val message: String,
    val timestamp: Long,
    override val type: String = TYPE_CHAT_MESSAGE
) : BaseModel()
