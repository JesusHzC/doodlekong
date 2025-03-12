package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CUR_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
): BaseModel(TYPE_CUR_ROUND_DRAW_INFO)
