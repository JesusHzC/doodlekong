package com.jesushz.doodlekong.setup.presentation.username

sealed interface UsernameAction {

    data class OnUsernameChanged(val username: String) : UsernameAction
    data object OnNextClicked : UsernameAction

}
