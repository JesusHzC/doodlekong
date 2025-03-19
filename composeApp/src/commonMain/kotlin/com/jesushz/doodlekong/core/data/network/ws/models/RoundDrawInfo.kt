package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CUR_ROUND_DRAW_INFO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoundDrawInfo(
    val data: List<String>,
    override val type: String = TYPE_CUR_ROUND_DRAW_INFO
): BaseModel()
