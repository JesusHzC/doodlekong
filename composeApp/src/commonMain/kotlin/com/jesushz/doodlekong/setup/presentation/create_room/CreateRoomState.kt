package com.jesushz.doodlekong.setup.presentation.create_room

data class CreateRoomState(
    val username: String = "",
    val roomName: String = "",
    val roomSize: Int = 2,
    val isLoading: Boolean = false
)
