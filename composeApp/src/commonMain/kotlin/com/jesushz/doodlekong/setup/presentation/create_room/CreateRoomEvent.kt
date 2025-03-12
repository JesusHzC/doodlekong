package com.jesushz.doodlekong.setup.presentation.create_room

import com.jesushz.doodlekong.core.presentation.UiText

sealed interface CreateRoomEvent {

    data object InputEmptyError: CreateRoomEvent
    data object InputTooLongError: CreateRoomEvent
    data object InputTooShortError: CreateRoomEvent
    data class OnError(val error: UiText): CreateRoomEvent
    data class JoinRoom(val username: String, val roomName: String): CreateRoomEvent

}
