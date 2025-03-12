package com.jesushz.doodlekong.core.data.network.responses

import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse(
    val successful: Boolean,
    val message: String? = null
)
