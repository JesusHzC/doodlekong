package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_ANNOUNCEMENT
import kotlinx.serialization.Serializable

@Serializable
data class Announcement(
    val message: String,
    val timestamp: Long,
    val announcementType: Int,
    override val type: String = TYPE_ANNOUNCEMENT
): BaseModel() {
    companion object {
        const val TYPE_PLAYER_GUESSED_WORD = 0
        const val TYPE_PLAYER_JOINED = 1
        const val TYPE_PLAYER_LEFT = 2
        const val TYPE_EVERYBODY_GUESSED_IT = 3
    }
}
