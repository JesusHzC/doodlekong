package com.jesushz.doodlekong.setup.presentation.username

sealed interface UsernameEvent {

    data object InputEmptyError: UsernameEvent
    data object InputTooShortError: UsernameEvent
    data object InputTooLongError: UsernameEvent
    data class NavigateToSelectRoomEvent(val username: String): UsernameEvent

}
