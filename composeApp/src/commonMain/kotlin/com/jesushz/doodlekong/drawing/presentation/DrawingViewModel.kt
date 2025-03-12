package com.jesushz.doodlekong.drawing.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DrawingViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                username = savedStateHandle.toRoute<Route.Drawing>().username,
                roomName = savedStateHandle.toRoute<Route.Drawing>().roomName
            )
        }
    }

    fun onAction(action: DrawingAction) {

    }

}