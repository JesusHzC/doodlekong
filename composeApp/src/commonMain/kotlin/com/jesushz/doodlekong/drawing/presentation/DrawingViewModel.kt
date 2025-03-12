package com.jesushz.doodlekong.drawing.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.jesushz.doodlekong.core.data.network.ws.models.Announcement
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.data.network.ws.models.ChosenWord
import com.jesushz.doodlekong.core.data.network.ws.models.DrawAction
import com.jesushz.doodlekong.core.data.network.ws.models.DrawData
import com.jesushz.doodlekong.core.data.network.ws.models.GameError
import com.jesushz.doodlekong.core.data.network.ws.models.GameState
import com.jesushz.doodlekong.core.data.network.ws.models.NewWords
import com.jesushz.doodlekong.core.data.network.ws.models.PhaseChange
import com.jesushz.doodlekong.core.data.network.ws.models.Ping
import com.jesushz.doodlekong.core.data.network.ws.models.PlayersList
import com.jesushz.doodlekong.core.data.network.ws.models.RoundDrawInfo
import com.jesushz.doodlekong.drawing.data.network.RealTimeDrawingClient
import com.jesushz.doodlekong.util.DispatcherProvider
import com.jesushz.doodlekong.util.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrawingViewModel(
    private val drawingClient: RealTimeDrawingClient,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state
        .onStart {
            observeBaseModel()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _state.value
        )

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

    private fun observeBaseModel() {
        viewModelScope.launch(dispatcherProvider.default) {
            drawingClient
                .getBaseModel()
                .onEach { model ->
                    Logger.i("ObserveBaseModel: $model")
                    when (model) {
                        is DrawData -> {

                        }
                        is ChatMessage -> {

                        }
                        is ChosenWord -> {

                        }
                        is RoundDrawInfo -> {

                        }
                        is Announcement -> {

                        }
                        is GameState -> {

                        }
                        is PlayersList -> {

                        }
                        is NewWords -> {

                        }
                        is DrawAction -> {

                        }
                        is PhaseChange -> {

                        }
                        is GameError -> {

                        }
                        is Ping -> {

                        }
                    }
                }
                .launchIn(this@launch)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(dispatcherProvider.io) {
            drawingClient.close()
        }
    }

}