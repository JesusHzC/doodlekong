package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.presentation.components.DoodleKongScaffold
import com.jesushz.doodlekong.drawing.presentation.components.DrawScreen
import com.jesushz.doodlekong.util.getSystemTime
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
    DoodleKongScaffold { innerPadding ->
        DrawScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 16.dp),
            cutWord = "_ _ _ _ _ _ _ _ _ _ _",
            chatText = "",
            micIsAvailable = false,
            chats = listOf(
                ChatMessage(
                    from = "Player 1",
                    message = "Hello",
                    timestamp = getSystemTime(),
                    roomName = "Room"
                )
            ),
            onChatTextChanged = {},
            onClearClick = {},
            onSendClick = {},
            onMicClick = {},
            onPlayerClick = {}
        )
    }
}
