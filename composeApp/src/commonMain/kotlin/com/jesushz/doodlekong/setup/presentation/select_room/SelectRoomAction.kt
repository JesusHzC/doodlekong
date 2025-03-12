package com.jesushz.doodlekong.setup.presentation.select_room

import com.jesushz.doodlekong.core.data.network.ws.Room

sealed interface SelectRoomAction {

    data class OnSearchRoomChanged(val searchRoom: String) : SelectRoomAction
    data object OnCreateRoomClicked : SelectRoomAction
    data class OnRoomClicked(val room: Room) : SelectRoomAction
    data object OnReloadClicked : SelectRoomAction

}
