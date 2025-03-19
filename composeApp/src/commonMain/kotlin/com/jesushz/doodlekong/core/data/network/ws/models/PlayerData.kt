package com.jesushz.doodlekong.core.data.network.ws.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    val username: String,
    var isDrawing: Boolean = false,
    var score: Int = 0,
    var rank: Int = 0
)
