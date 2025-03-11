package com.jesushz.doodlekong.setup.presentation.select_room

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.doodlekong.setup.presentation.components.DoodleKongTextField
import com.jesushz.doodlekong.setup.presentation.select_room.components.EmptyRooms
import com.jesushz.doodlekong.setup.presentation.select_room.components.RoomsList
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.create_room
import doodlekong.composeapp.generated.resources.ic_refresh
import doodlekong.composeapp.generated.resources.or
import doodlekong.composeapp.generated.resources.refresh
import doodlekong.composeapp.generated.resources.search_for_rooms
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectRoomScreenRoot(
    viewModel: SelectRoomViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SelectRoomScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SelectRoomScreen(
    state: SelectRoomState,
    onAction: (SelectRoomAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DoodleKongTextField(
                text = state.searchRoom,
                modifier = Modifier
                    .weight(1f),
                label = stringResource(Res.string.search_for_rooms),
                onTextChanged = {
                    onAction(SelectRoomAction.OnSearchRoomChanged(it))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(Res.drawable.ic_refresh),
                contentDescription = stringResource(Res.string.refresh)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        when {
            state.rooms.isNotEmpty() -> {
                AnimatedVisibility(
                    visible = state.rooms.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    RoomsList(
                        rooms = state.rooms,
                        modifier = Modifier
                            .fillMaxSize(),
                        onRoomClick = {

                        }
                    )
                }
            }
            else -> {
                AnimatedVisibility(
                    visible = state.rooms.isEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    EmptyRooms(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.or),
                fontSize = 14.sp
            )
            TextButton(
                onClick = {
                    onAction(SelectRoomAction.OnCreateRoomClicked)
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(Res.string.create_room).uppercase(),
                    fontSize = 16.sp
                )
            }
        }
    }
}
