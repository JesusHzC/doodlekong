package com.jesushz.doodlekong.setup.presentation.select_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jesushz.doodlekong.core.domain.onError
import com.jesushz.doodlekong.core.domain.onSuccess
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectRoomViewModel(
    private val setupRepository: SetupRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(SelectRoomState())
    val state = _state.asStateFlow()

    private var jobRooms: Job? = null

    init {
        _state.update {
            it.copy(
                username = savedStateHandle.toRoute<Route.SelectRoom>().username
            )
        }
    }

    fun onAction(action: SelectRoomAction) {

    }

    fun getRooms(searchQuery: String) {
        jobRooms?.cancel()
        jobRooms = viewModelScope.launch {
            setupRepository.getRooms(searchQuery)
                .onSuccess { rooms ->
                    _state.update {
                        it.copy(
                            rooms = rooms
                        )
                    }
                }
                .onError { error ->

                }
        }
    }

}