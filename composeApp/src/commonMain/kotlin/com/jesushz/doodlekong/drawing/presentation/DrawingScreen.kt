package com.jesushz.doodlekong.drawing.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.presentation.components.DoodleKongScaffold
import com.jesushz.doodlekong.drawing.data.models.drawingColors
import com.jesushz.doodlekong.drawing.presentation.components.ChatSection
import com.jesushz.doodlekong.drawing.presentation.components.DrawingCanvas
import com.jesushz.doodlekong.drawing.presentation.components.DrawingControls
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1.5f)
            ) {
                DrawingControls(
                    modifier = Modifier
                        .fillMaxWidth(),
                    selectedColor = state.selectedColor,
                    colors = drawingColors,
                    onSelectedColor = {
                        onAction(DrawingAction.OnSelectedColor(it))
                    },
                    onClearCanvas = {
                        onAction(DrawingAction.OnClearCanvasClick)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DrawingCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    paths = state.paths,
                    currentPath = state.currentPath,
                    onDragStart = {
                        onAction(DrawingAction.OnNewPathStart)
                    },
                    onDragEnd = {
                        onAction(DrawingAction.OnPathEnd)
                    },
                    onDrag = {
                        onAction(DrawingAction.OnDraw(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                progress = 1f,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            ChatSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .weight(1f),
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
}
