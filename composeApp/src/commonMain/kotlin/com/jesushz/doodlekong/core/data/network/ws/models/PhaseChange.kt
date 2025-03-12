package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PHASE_CHANGE
import com.jesushz.doodlekong.core.data.network.ws.Room

data class PhaseChange(
        var phase: Room.Phase?,
        var time: Long,
        val drawingPlayer: String? = null
) : BaseModel(TYPE_PHASE_CHANGE)
