package com.jesushz.doodlekong.setup.presentation.create_room

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.doodlekong.core.presentation.ObserveAsEvents
import com.jesushz.doodlekong.core.presentation.components.DoodleKongButton
import com.jesushz.doodlekong.core.presentation.components.DoodleKongScaffold
import com.jesushz.doodlekong.core.presentation.components.DoodleKongTextField
import com.jesushz.doodlekong.setup.presentation.create_room.components.RoomSizeDropDown
import com.jesushz.doodlekong.util.Constants
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.create_a_new_room
import doodlekong.composeapp.generated.resources.create_room
import doodlekong.composeapp.generated.resources.ic_doodle_world
import doodlekong.composeapp.generated.resources.room_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateRoomScreenRoot(
    viewModel: CreateRoomViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDrawingScreen: (username: String, roomName: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            CreateRoomEvent.InputEmptyError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.INPUT_EMPTY_ERROR
                    )
                }
            }
            CreateRoomEvent.InputTooLongError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.ERROR_ROOM_NAME_TOO_LONG
                    )
                }
            }
            CreateRoomEvent.InputTooShortError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.ERROR_ROOM_NAME_TOO_SHORT
                    )
                }
            }
            is CreateRoomEvent.JoinRoom -> {
                onNavigateToDrawingScreen(
                    event.username,
                    event.roomName
                )
            }
            is CreateRoomEvent.OnError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.error.asDynamicString()
                    )
                }
            }
        }
    }
    CreateRoomScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                CreateRoomAction.OnBackClicked -> {
                    onNavigateBack()
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun CreateRoomScreen(
    state: CreateRoomState,
    snackBarHostState: SnackbarHostState,
    onAction: (CreateRoomAction) -> Unit,
) {
    DoodleKongScaffold(
        showBackButton = true,
        onBackClicked = {
            onAction(CreateRoomAction.OnBackClicked)
        },
        snackBarHostState = snackBarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_doodle_world),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.create_a_new_room),
                fontSize = 50.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            DoodleKongTextField(
                text = state.roomName,
                label = stringResource(Res.string.room_name),
                onTextChanged = {
                    onAction(CreateRoomAction.OnRoomNameChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoomSizeDropDown(
                    onRoomSizeSelected = { size ->
                        onAction(CreateRoomAction.OnRoomSizeChanged(size))
                    },
                    modifier = Modifier
                        .width(130.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                DoodleKongButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(Res.string.create_room).uppercase(),
                    onButtonClicked = {
                        onAction(CreateRoomAction.OnCreateRoomClicked)
                    },
                    isLoading = state.isLoading
                )
            }
        }
    }
}
