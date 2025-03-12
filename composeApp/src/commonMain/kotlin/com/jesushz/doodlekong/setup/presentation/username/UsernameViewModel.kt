package com.jesushz.doodlekong.setup.presentation.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesushz.doodlekong.util.Constants.MAX_USERNAME_LENGTH
import com.jesushz.doodlekong.util.Constants.MIN_USERNAME_LENGTH
import com.jesushz.doodlekong.util.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsernameViewModel(
    private val dispatchers: DispatcherProvider
): ViewModel() {

    private val _state = MutableStateFlow(UsernameState())
    val state = _state.asStateFlow()

    private val _event = Channel<UsernameEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: UsernameAction) {
        when (action) {
            UsernameAction.OnNextClicked -> {
                validateUsernameAndNavigateToSelectRoom(_state.value.username)
            }
            is UsernameAction.OnUsernameChanged -> {
                _state.update {
                    it.copy(username = action.username)
                }
            }
        }
    }

    private fun validateUsernameAndNavigateToSelectRoom(username: String) {
        viewModelScope.launch(dispatchers.main) {
            val trimmedUsername = username.trim()
            when {
                trimmedUsername.isEmpty() -> {
                    _event.send(UsernameEvent.InputEmptyError)
                }
                trimmedUsername.length < MIN_USERNAME_LENGTH -> {
                    _event.send(UsernameEvent.InputTooShortError)
                }
                trimmedUsername.length > MAX_USERNAME_LENGTH -> {
                    _event.send(UsernameEvent.InputTooLongError)
                }
                else -> _event.send(UsernameEvent.NavigateToSelectRoomEvent(trimmedUsername))
            }
        }
    }

}