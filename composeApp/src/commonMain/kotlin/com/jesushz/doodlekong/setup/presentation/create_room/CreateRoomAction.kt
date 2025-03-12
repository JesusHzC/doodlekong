package com.jesushz.doodlekong.setup.presentation.create_room

sealed interface CreateRoomAction {

    data object OnBackClicked : CreateRoomAction
    data class OnRoomNameChanged(val roomName: String) : CreateRoomAction
    data class OnRoomSizeChanged(val roomSize: Int) : CreateRoomAction
    data object OnCreateRoomClicked : CreateRoomAction

}