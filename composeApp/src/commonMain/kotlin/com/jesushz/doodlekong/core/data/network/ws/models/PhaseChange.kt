package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PHASE_CHANGE
import com.jesushz.doodlekong.core.data.network.ws.Room
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhaseChange(
        var phase: Room.Phase? = null,
        var time: Long,
        val drawingPlayer: String? = null,
        override val type: String = TYPE_PHASE_CHANGE
) : BaseModel()
