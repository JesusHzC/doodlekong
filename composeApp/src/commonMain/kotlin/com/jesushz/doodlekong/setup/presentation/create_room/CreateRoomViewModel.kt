package com.jesushz.doodlekong.setup.presentation.create_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.domain.onError
import com.jesushz.doodlekong.core.domain.onSuccess
import com.jesushz.doodlekong.core.presentation.toUiText
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository
import com.jesushz.doodlekong.util.Constants.MAX_ROOM_NAME_LENGTH
import com.jesushz.doodlekong.util.Constants.MIN_ROOM_NAME_LENGTH
import com.jesushz.doodlekong.util.DispatcherProvider
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateRoomViewModel(
    private val dispatcherProvider: DispatcherProvider,
    private val setupRepository: SetupRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(CreateRoomState())
    val state = _state.asStateFlow()

    private val _event = Channel<CreateRoomEvent>()
    val event = _event.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                username = savedStateHandle.toRoute<Route.CreateRoom>().username
            )
        }
    }

    fun onAction(action: CreateRoomAction) {
        when (action) {
            CreateRoomAction.OnCreateRoomClicked -> {
                val roomName = _state.value.roomName
                val roomSize = _state.value.roomSize
                val username = _state.value.username
                createRoom(roomName, roomSize, username)
            }
            is CreateRoomAction.OnRoomNameChanged -> {
                _state.update {
                    it.copy(
                        roomName = action.roomName
                    )
                }
            }
            is CreateRoomAction.OnRoomSizeChanged -> {
                _state.update {
                    it.copy(
                        roomSize = action.roomSize
                    )
                }
            }
            else -> Unit
        }
    }

    private fun createRoom(
        roomName: String,
        roomSize: Int,
        username: String
    ) {
        viewModelScope.launch(
            dispatcherProvider.main
        ) {
            val trimmedRoomName = roomName.trim()
            when {
                trimmedRoomName.isEmpty() -> {
                    _event.send(CreateRoomEvent.InputEmptyError)
                }
                trimmedRoomName.length < MIN_ROOM_NAME_LENGTH -> {
                    _event.send(CreateRoomEvent.InputTooShortError)
                }
                trimmedRoomName.length > MAX_ROOM_NAME_LENGTH -> {
                    _event.send(CreateRoomEvent.InputTooLongError)
                }
                else -> {
                    val room = Room(
                        name = trimmedRoomName,
                        maxPlayers = roomSize
                    )
                    isLoading(true)
                    setupRepository
                        .createRoom(room)
                        .onSuccess {
                            joinRoom(username, trimmedRoomName)
                        }
                        .onError { error ->
                            isLoading(false)
                            _event.send(CreateRoomEvent.OnError(error.toUiText()))
                        }
                }
            }
        }
    }

    private fun joinRoom(username: String, roomName: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            setupRepository
                .joinRoom(username, roomName)
                .onSuccess {
                    isLoading(false)
                    _event.send(CreateRoomEvent.JoinRoom(username, roomName))
                }
                .onError { error ->
                    isLoading(false)
                    _event.send(CreateRoomEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

}