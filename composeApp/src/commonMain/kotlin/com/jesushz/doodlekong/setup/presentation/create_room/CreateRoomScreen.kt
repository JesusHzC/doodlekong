package com.jesushz.doodlekong.setup.presentation.create_room

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateRoomScreenRoot(
    viewModel: CreateRoomViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateRoomScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun CreateRoomScreen(
    state: CreateRoomState,
    onAction: (CreateRoomAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = state.username,
        )
    }
}
