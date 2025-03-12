package com.jesushz.doodlekong.setup.presentation.select_room

import com.jesushz.doodlekong.core.presentation.UiText

sealed interface SelectRoomEvent {

    data class OnError(val message: UiText) : SelectRoomEvent
    data class OnJoinRoom(val username: String, val roomName: String) : SelectRoomEvent

}