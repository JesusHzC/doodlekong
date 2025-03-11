package com.jesushz.doodlekong.setup.presentation.select_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectRoomViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(SelectRoomState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                username = savedStateHandle.toRoute<Route.SelectRoom>().username
            )
        }
    }

    fun onAction(action: SelectRoomAction) {

    }

}