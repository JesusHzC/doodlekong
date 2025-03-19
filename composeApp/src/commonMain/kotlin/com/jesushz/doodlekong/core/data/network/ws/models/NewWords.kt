package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_NEW_WORDS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewWords(
    val newWords: List<String>,
    override val type: String = TYPE_NEW_WORDS
): BaseModel()
