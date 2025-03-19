package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DISCONNECT_REQUEST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DisconnectRequest(
    override val type: String = TYPE_DISCONNECT_REQUEST
) : BaseModel()
