package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerValue
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jesushz.doodlekong.core.data.network.ws.models.GameError
import com.jesushz.doodlekong.core.presentation.ObserveAsEvents
import com.jesushz.doodlekong.core.presentation.components.DoodleKongButton
import com.jesushz.doodlekong.core.presentation.components.DoodleKongScaffold
import com.jesushz.doodlekong.core.presentation.components.LoadingAnimation
import com.jesushz.doodlekong.drawing.data.models.drawingColors
import com.jesushz.doodlekong.drawing.presentation.components.ChatSection
import com.jesushz.doodlekong.drawing.presentation.components.DrawingCanvas
import com.jesushz.doodlekong.drawing.presentation.components.DrawingControls
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.choose_your_word
import doodlekong.composeapp.generated.resources.ic_happy_monkey
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DrawingScreenRoot(
    viewModel: DrawingViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scaffoldState = rememberScaffoldState(
        drawerState = drawerState
    )
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            is DrawingEvent.OnGameError -> {
                if (event.error.errorType == GameError.ERROR_ROOM_NOT_FOUND) {
                    onNavigateBack()
                }
            }
            is DrawingEvent.OnConnectionError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(message = event.error)
                }
            }
        }
    }
    DrawingScreen(
        state = state,
        scaffoldState = scaffoldState,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                DrawingAction.OnPlayersClick -> {
                    scope.launch {
                        drawerState.open()
                    }
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun DrawingScreen(
    state: DrawingState,
    snackBarHostState: SnackbarHostState,
    scaffoldState: ScaffoldState,
    onAction: (DrawingAction) -> Unit,
) {
    DoodleKongScaffold(
        scaffoldState = scaffoldState,
        snackBarHostState = snackBarHostState,
        showNavigationDrawer = true,
        players = state.players,
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        when {
            state.showChooseWordScreen -> {
                ChooseWordScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    state = state,
                    onAction = onAction
                )
            }
            else -> {
                DrawScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(vertical = 16.dp),
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
internal fun ChooseWordScreen(
    modifier: Modifier,
    state: DrawingState,
    onAction: (DrawingAction) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_happy_monkey),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.choose_your_word),
            fontSize = 35.sp
        )
        Spacer(modifier = Modifier.height(30.dp))
        var buttonSelected by remember { mutableIntStateOf(-1) }
        state.newWords.fastForEachIndexed { index, option ->
            DoodleKongButton(
                modifier = Modifier
                    .width(200.dp),
                text = option,
                isSelected = buttonSelected == index,
                onButtonClicked = {
                    buttonSelected = index
                    onAction(DrawingAction.OnNewWordSelected(option))
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = (state.progressBarValue / 1000L)
                .toFloat()
                .roundToInt()
                .toString(),
            fontSize = 50.sp
        )
    }
}

@Composable
internal fun DrawScreen(
    modifier: Modifier,
    state: DrawingState,
    onAction: (DrawingAction) -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)
    val permissionsViewModel = viewModel {
        PermissionsViewModel(controller)
    }
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1.5f)
        ) {
            if (state.showDrawingActions) {
                DrawingControls(
                    modifier = Modifier
                        .fillMaxWidth(),
                    selectedColor = state.selectedColor,
                    colors = drawingColors,
                    onSelectedColor = {
                        onAction(DrawingAction.OnSelectedColor(it))
                    },
                    onClearCanvas = {
                        onAction(DrawingAction.OnUndo)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            DrawingCanvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                paths = state.paths,
                currentPath = state.currentPath,
                dragIsAvailable = state.canvasEnabled,
                onDragStart = {
                    onAction(DrawingAction.OnNewPathStart(it))
                },
                onDragEnd = {
                    onAction(DrawingAction.OnPathEnd)
                },
                onDrag = {
                    onAction(DrawingAction.OnDrag(it))
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        TimedProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            currentTime = state.progressBarValue,
            maxTime = state.maxProgressBar
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (state.showConnectionProgress) {
            LoadingAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            ChatSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                username = state.username,
                cutWord = state.cutWord,
                chatText = state.message,
                micIsAvailable = permissionsViewModel.state == PermissionState.Granted,
                chatObjects = state.chatObjects,
                showSendChatMessage = state.showSendChatMessage,
                showMic = state.showMic,
                onChatTextChanged = {
                    onAction(DrawingAction.OnMessageChanged(it))
                },
                onClearClick = {
                    onAction(DrawingAction.OnClearClick)
                },
                onSendClick = {
                    onAction(DrawingAction.OnSendClick)
                },
                onMicClick = {
                    when (permissionsViewModel.state) {
                        PermissionState.Granted -> {
                            onAction(DrawingAction.OnStartRecording)
                        }
                        PermissionState.DeniedAlways -> {
                            controller.openAppSettings()
                        }
                        else -> {
                            permissionsViewModel.provideOrRequestRecordAudioPermission()
                        }
                    }
                },
                onPlayerClick = {
                    onAction(DrawingAction.OnPlayersClick)
                }
            )
        }
    }
}

@Composable
fun TimedProgressBar(currentTime: Long, maxTime: Long, modifier: Modifier = Modifier) {
    val progress = (currentTime.toFloat() / maxTime.toFloat()).coerceIn(0f, 1f)

    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = Color.Black
    )
}
