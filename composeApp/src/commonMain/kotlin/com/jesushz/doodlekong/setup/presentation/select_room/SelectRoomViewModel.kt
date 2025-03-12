package com.jesushz.doodlekong.setup.presentation.select_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.doodlekong.core.domain.onError
import com.jesushz.doodlekong.core.domain.onSuccess
import com.jesushz.doodlekong.core.presentation.toUiText
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository
import com.jesushz.doodlekong.util.DispatcherProvider
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectRoomViewModel(
    private val setupRepository: SetupRepository,
    private val dispatchers: DispatcherProvider,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(SelectRoomState())
    val state = _state
        .onStart {
            getRooms(_state.value.searchRoom)
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _state.value
        )

    private val _event = Channel<SelectRoomEvent>()
    val event = _event.receiveAsFlow()

    private var jobRooms: Job? = null

    init {
        _state.update {
            it.copy(
                username = savedStateHandle.toRoute<Route.SelectRoom>().username
            )
        }
    }

    fun onAction(action: SelectRoomAction) {
        when (action) {
            is SelectRoomAction.OnRoomClicked -> {
                val username = _state.value.username
                val roomName = action.room.name
                joinRoom(username, roomName)
            }
            is SelectRoomAction.OnSearchRoomChanged -> {
                _state.update {
                    it.copy(
                        searchRoom = action.searchRoom
                    )
                }
                getRooms(action.searchRoom)
            }
            SelectRoomAction.OnReloadClicked -> {
                val query = _state.value.searchRoom
                getRooms(query)
            }
            else -> Unit
        }
    }

    private fun getRooms(searchQuery: String) {
        jobRooms?.cancel()
        jobRooms = viewModelScope.launch(dispatchers.main) {
            delay(300L)
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            setupRepository.getRooms(searchQuery)
                .onSuccess { rooms ->
                    _state.update {
                        it.copy(
                            rooms = rooms,
                            isLoading = false
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _event.send(
                        SelectRoomEvent.OnError(error.toUiText())
                    )
                }
        }
    }

    private fun joinRoom(username: String, roomName: String) {
        viewModelScope.launch(dispatchers.main) {
            setupRepository
                .joinRoom(username, roomName)
                .onSuccess {
                    _event.send(
                        SelectRoomEvent.OnJoinRoom(username, roomName)
                    )
                }
                .onError { error ->
                    _event.send(
                        SelectRoomEvent.OnError(error.toUiText())
                    )
                }
        }
    }

}