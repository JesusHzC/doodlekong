package com.jesushz.doodlekong.core.data.network.ws.models

import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
): BaseModel(TYPE_CHOSEN_WORD)
