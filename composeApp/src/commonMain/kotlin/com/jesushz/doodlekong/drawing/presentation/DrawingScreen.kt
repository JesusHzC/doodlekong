package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DrawingScreenRoot(
    viewModel: DrawingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DrawingScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun DrawingScreen(
    state: DrawingState,
    onAction: (DrawingAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Text(
                text = state.username,
            )
            Text(
                text = state.roomName
            )
        }
    }
}
