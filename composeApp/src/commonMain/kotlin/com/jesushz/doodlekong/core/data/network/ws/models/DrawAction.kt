package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_ACTION
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DrawAction(
    val action: String,
    override val type: String = TYPE_DRAW_ACTION
): BaseModel() {

    companion object {
        const val ACTION_UNDO = "ACTION_UNDO"
    }
}
