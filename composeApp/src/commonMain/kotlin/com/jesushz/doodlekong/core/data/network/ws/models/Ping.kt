package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Ping(
    override val type: String = TYPE_PING
) : BaseModel()