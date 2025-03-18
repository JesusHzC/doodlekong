package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.jesushz.doodlekong.util.getSystemTime
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
        when (action) {
            DrawingAction.OnClearCanvasClick -> clearCanvas()
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnNewPathStart -> onNewPathStart()
            DrawingAction.OnPathEnd -> onPathEnd()
            is DrawingAction.OnSelectedColor -> onSelectedColor(action.color)
        }
    }

    private fun clearCanvas() {
        _state.update {
            it.copy(
                paths = emptyList(),
                currentPath = null
            )
        }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        }
    }

    private fun onNewPathStart() {
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = getSystemTime().toString(),
                    color = it.selectedColor,
                    path = emptyList()
                )
            )
        }
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPathData
            )
        }
    }

    private fun onSelectedColor(color: Color) {
        _state.update {
            it.copy(
                selectedColor = color
            )
        }
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