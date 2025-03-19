package com.jesushz.doodlekong.core.data.network.ws.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseModel {
    abstract val type: String
}
