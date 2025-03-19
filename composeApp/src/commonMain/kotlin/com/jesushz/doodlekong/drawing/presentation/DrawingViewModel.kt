package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_ACTION
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_DATA
import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.data.network.ws.models.Announcement
import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.data.network.ws.models.ChosenWord
import com.jesushz.doodlekong.core.data.network.ws.models.DisconnectRequest
import com.jesushz.doodlekong.core.data.network.ws.models.DrawAction
import com.jesushz.doodlekong.core.data.network.ws.models.DrawAction.Companion.ACTION_UNDO
import com.jesushz.doodlekong.core.data.network.ws.models.DrawData
import com.jesushz.doodlekong.core.data.network.ws.models.GameError
import com.jesushz.doodlekong.core.data.network.ws.models.GameState
import com.jesushz.doodlekong.core.data.network.ws.models.JoinRoomHandshake
import com.jesushz.doodlekong.core.data.network.ws.models.NewWords
import com.jesushz.doodlekong.core.data.network.ws.models.PhaseChange
import com.jesushz.doodlekong.core.data.network.ws.models.Ping
import com.jesushz.doodlekong.core.data.network.ws.models.PlayersList
import com.jesushz.doodlekong.core.data.network.ws.models.RoundDrawInfo
import com.jesushz.doodlekong.drawing.data.mappers.toPathData
import com.jesushz.doodlekong.drawing.data.network.RealTimeDrawingClient
import com.jesushz.doodlekong.util.Constants
import com.jesushz.doodlekong.util.CoroutineTimer
import com.jesushz.doodlekong.util.DispatcherProvider
import com.jesushz.doodlekong.util.MotionEvent.ACTION_DOWN
import com.jesushz.doodlekong.util.MotionEvent.ACTION_MOVE
import com.jesushz.doodlekong.util.MotionEvent.ACTION_UP
import com.jesushz.doodlekong.util.Route
import com.jesushz.doodlekong.util.getSystemTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class DrawingViewModel(
    private val drawingClient: RealTimeDrawingClient,
    private val dispatcherProvider: DispatcherProvider,
    private val clientId: String,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state
        .onStart {
            observeBaseModel()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    private val _event = Channel<DrawingEvent>()
    val event = _event.receiveAsFlow()

    private val timer = CoroutineTimer()
    private var timerJob: Job? = null

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
            is DrawingAction.OnDrag -> onDrag(action.offset)
            is DrawingAction.OnNewPathStart -> onNewPathStart()
            DrawingAction.OnPathEnd -> onPathEnd()
            is DrawingAction.OnSelectedColor -> onSelectedColor(action.color)
            is DrawingAction.OnMessageChanged -> {
                _state.update {
                    it.copy(
                        message = action.message
                    )
                }
            }
            DrawingAction.OnClearClick -> clearText()
            DrawingAction.OnSendClick -> {
                val message = state.value.message
                val username = state.value.username
                val roomName = state.value.roomName
                val currentTime = getSystemTime()
                val chatMessage = ChatMessage(
                    username,
                    roomName,
                    message,
                    currentTime
                )
                sendChatMessage(chatMessage)
                clearText()
            }
            DrawingAction.OnUndo -> onUndo()
            is DrawingAction.OnNewWordSelected -> onNewWordSelected(action.word)
        }
    }

    private fun onNewWordSelected(word: String) {
        val roomName = state.value.roomName
        chooseWord(word, roomName)
        _state.update {
            it.copy(
                showChooseWordScreen = false
            )
        }
    }

    private fun clearText() {
        _state.update {
            it.copy(
                message = ""
            )
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

    private fun onUndo() {
        val isUserDrawing = state.value.isUserDrawing

        if (isUserDrawing) {
            val paths = state.value.paths.toMutableList()
            if (paths.isEmpty()) return

            paths.removeLastOrNull()
            _state.update {
                it.copy(
                    paths = paths
                )
            }
            sendBaseModel(DrawAction(ACTION_UNDO))
        }
    }

    private fun onDrag(offset: Offset) {
        val canvasIsEnable = state.value.canvasEnabled
        if (!canvasIsEnable) return
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        }
        val isUserDrawing = state.value.isUserDrawing
        if (isUserDrawing) {
            val roomName = state.value.roomName
            val color = state.value.selectedColor
            val pathData = state.value.currentPath ?: return
            val fromX = pathData.path.last().x
            val fromY = pathData.path.last().y
            val toX = offset.x
            val toY = offset.y
            val drawData = DrawData(
                roomName = roomName,
                color = color.toArgb(),
                thickness = 10f,
                fromX = fromX,
                fromY = fromY,
                toX = toX,
                toY = toY,
                motionEvent = ACTION_MOVE
            )
            sendBaseModel(drawData)
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
        val currentPathData = state.value.currentPath ?: return
        val drawData = DrawData(
            roomName = state.value.roomName,
            color = currentPathData.color.toArgb(),
            thickness = 10f,
            fromX = 0f,
            fromY = 0f,
            toX = 0f,
            toY = 0f,
            motionEvent = ACTION_DOWN
        )
        sendBaseModel(drawData)
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPathData
            )
        }
        val drawData = DrawData(
            roomName = state.value.roomName,
            color = currentPathData.color.toArgb(),
            thickness = 10f,
            fromX = currentPathData.path.first().x,
            fromY = currentPathData.path.first().y,
            toX = currentPathData.path.last().x,
            toY = currentPathData.path.last().y,
            motionEvent = ACTION_UP
        )
        sendBaseModel(drawData)
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
                .getBaseModel(
                    onConnectionError = {
                        viewModelScope.launch {
                            _event.send(
                                DrawingEvent.OnConnectionError(it.message ?: Constants.ERROR_UNKNOWN)
                            )
                        }
                        setConnectionProgress(false)
                    },
                    onConnectionSuccess = {
                        sendBaseModel(
                            JoinRoomHandshake(
                                username = state.value.username,
                                roomName = state.value.roomName,
                                clientId = clientId
                            )
                        )
                        setConnectionProgress(false)
                    }
                )
                .onEach { data ->
                    Logger.i("ObserveBaseModel: $data")
                    when (data) {
                        is DrawData -> {
                            val isUserDrawing = state.value.isUserDrawing
                            if (!isUserDrawing) {
                                when (data.motionEvent) {
                                    ACTION_DOWN -> {
                                        onNewPathStart()
                                    }
                                    ACTION_MOVE -> {
                                        updateCanvasFromServer(listOf(data))
                                    }
                                    ACTION_UP -> {
                                        onPathEnd()
                                    }
                                }
                            }
                        }
                        is ChatMessage -> {
                            _state.update {
                                it.copy(
                                    chatObjects = it.chatObjects + data
                                )
                            }
                        }
                        is ChosenWord -> {
                            _state.update {
                                it.copy(
                                    cutWord = data.chosenWord
                                )
                            }
                        }
                        is RoundDrawInfo -> {
                            val json = Json { ignoreUnknownKeys = true }

                            val listBaseModel = data.data.mapNotNull { drawAction ->
                                runCatching {
                                    val jsonObject = json.decodeFromString<JsonObject>(drawAction)
                                    val type = when (jsonObject["type"]?.jsonObject?.get("type")?.toString()?.removeSurrounding("\"")) {
                                        TYPE_DRAW_DATA -> json.decodeFromString<DrawData>(drawAction)
                                        TYPE_DRAW_ACTION -> json.decodeFromString<DrawAction>(drawAction)
                                        else -> json.decodeFromString<BaseModel>(drawAction)
                                    }
                                    type
                                }.getOrNull()
                            }
                            updateCanvasFromServer(listBaseModel)
                        }
                        is Announcement -> {
                            _state.update {
                                it.copy(
                                    chatObjects = it.chatObjects + data
                                )
                            }
                        }
                        is GameState -> {
                            val username = state.value.username
                            val isUserDrawing = data.drawingPlayer == username
                            _state.update { state ->
                                state.copy(
                                    cutWord = data.word,
                                    isUserDrawing = isUserDrawing,
                                    showDrawingActions = isUserDrawing,
                                    showSendChatMessage = !isUserDrawing,
                                    showMic = !isUserDrawing,
                                    canvasEnabled = isUserDrawing
                                )
                            }
                            clearCanvas()
                        }
                        is PlayersList -> {
                            _state.update { state ->
                                state.copy(
                                    players = data.players
                                )
                            }
                        }
                        is NewWords -> {
                            if (data.newWords.isEmpty()) return@onEach
                            _state.update { state ->
                                state.copy(
                                    newWords = data.newWords
                                )
                            }
                        }
                        is DrawAction -> {
                            when (data.action) {
                                ACTION_UNDO -> {
                                    val paths = state.value.paths.toMutableList()
                                    if (paths.isEmpty()) return@onEach

                                    paths.removeLastOrNull()
                                    _state.update {
                                        it.copy(
                                            paths = paths
                                        )
                                    }
                                }
                            }
                        }
                        is PhaseChange -> {
                            data.phase?.let {
                                _state.update { state ->
                                    state.copy(
                                        phase = data
                                    )
                                }
                            }
                            observePhase(data)
                            _state.update { state ->
                                state.copy(
                                    progressBarValue = data.time
                                )
                            }
                            if(data.phase != Room.Phase.WAITING_FOR_PLAYERS) {
                                setTimer(data.time)
                            }
                        }
                        is GameError -> {
                            withContext(dispatcherProvider.main) {
                                _event.send(DrawingEvent.OnGameError(data))
                            }
                        }
                        is Ping -> {
                            sendBaseModel(Ping())
                        }
                        else -> Unit
                    }
                }
                .launchIn(this@launch)
        }
    }

    private fun updateCanvasFromServer(drawActions: List<BaseModel>) {
        drawActions.fastForEach { drawAction ->
            when (drawAction) {
                is DrawData -> {
                    when (drawAction.motionEvent) {
                        ACTION_DOWN -> {
                            onNewPathStart()
                        }
                        ACTION_MOVE -> {
                            val newPathData = drawAction.toPathData()
                            _state.update { currentState ->
                                val updatedCurrentPath = currentState.currentPath?.copy(
                                    path = currentState.currentPath.path + newPathData.path
                                ) ?: newPathData
                                currentState.copy(
                                    currentPath = updatedCurrentPath,
                                    selectedColor = newPathData.color
                                )
                            }
                        }
                        ACTION_UP -> {
                            onPathEnd()
                        }
                    }
                }
                is DrawAction -> {
                    when (drawAction.action) {
                        ACTION_UNDO -> {
                            val paths = state.value.paths.toMutableList()
                            if (paths.isEmpty()) return

                            paths.removeLastOrNull()
                            _state.update {
                                it.copy(
                                    paths = paths
                                )
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    private fun setTimer(duration: Long) {
        timerJob?.cancel()
        timerJob = timer.timeAndEmit(duration, viewModelScope) {
            _state.update { state ->
                state.copy(
                    progressBarValue = it
                )
            }
        }
    }

    private fun disconnect() {
        sendBaseModel(DisconnectRequest())
    }

    private fun chooseWord(word: String, roomName: String) {
        val chosenWord = ChosenWord(word, roomName)
        sendBaseModel(chosenWord)
    }

    private fun sendChatMessage(chat: ChatMessage) {
        viewModelScope.launch(dispatcherProvider.io) {
            if (chat.message.trim().isEmpty()) return@launch
            viewModelScope.launch(dispatcherProvider.io) {
                drawingClient.sendBaseModel(chat)
            }
        }
    }

    private fun sendBaseModel(baseModel: BaseModel) {
        viewModelScope.launch(dispatcherProvider.default) {
            drawingClient.sendBaseModel(baseModel)
        }
    }

    private fun setConnectionProgress(show: Boolean) {
        _state.update {
            it.copy(
                showConnectionProgress = show
            )
        }
    }

    private fun observePhase(phase: PhaseChange) {
        when (phase.phase) {
            Room.Phase.WAITING_FOR_PLAYERS -> {
                _state.update {
                    it.copy(
                        cutWord = "Waiting for players...",
                        showConnectionProgress = false,
                        progressBarValue = it.maxProgressBar
                    )
                }
                cancelTimer()
            }
            Room.Phase.WAITING_FOR_START -> {
                _state.update {
                    it.copy(
                        cutWord = "Waiting for start...",
                        maxProgressBar = phase.time
                    )
                }
            }
            Room.Phase.NEW_ROUND -> {
                phase.drawingPlayer?.let { player ->
                    _state.update {
                        it.copy(
                            cutWord = "$player is is choosing a word...",
                        )
                    }
                }
                _state.update {
                    it.copy(
                        selectedColor = Color.Black,
                        maxProgressBar = phase.time,
                        isUserDrawing = phase.drawingPlayer == it.username,
                        showChooseWordScreen = phase.drawingPlayer == it.username
                    )
                }
            }
            Room.Phase.GAME_RUNNING -> {
                _state.update {
                    it.copy(
                        maxProgressBar = phase.time,
                        showChooseWordScreen = false
                    )
                }
            }
            Room.Phase.SHOW_WORD -> {
                _state.update {
                    it.copy(
                        selectedColor = Color.Black,
                        maxProgressBar = phase.time
                    )
                }
            }
            else -> Unit
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(dispatcherProvider.io) {
            drawingClient.close()
        }
    }

}