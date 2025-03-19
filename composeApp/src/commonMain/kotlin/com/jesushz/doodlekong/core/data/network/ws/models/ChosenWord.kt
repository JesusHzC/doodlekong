package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CHOSEN_WORD
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChosenWord(
    val chosenWord: String,
    val roomName: String,
    override val type: String = TYPE_CHOSEN_WORD
): BaseModel()
