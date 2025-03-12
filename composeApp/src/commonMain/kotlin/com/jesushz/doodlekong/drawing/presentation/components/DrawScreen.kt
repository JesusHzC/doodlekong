package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage

@Composable
fun DrawScreen(
    modifier: Modifier = Modifier,
    cutWord: String,
    chatText: String,
    micIsAvailable: Boolean,
    chats: List<ChatMessage>,
    onChatTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    onPlayerClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1.5f)
        ) {
            DrawingActions(
                modifier = Modifier
                    .fillMaxWidth(),
                onActionSelected = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            DrawingCanvas(
                modifier = Modifier
                    .fillMaxSize(),
                onDraw = {},
                paths = emptyList()
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
            cutWord = cutWord,
            chatText = chatText,
            micIsAvailable = micIsAvailable,
            chats = chats,
            onChatTextChanged = onChatTextChanged,
            onClearClick = onClearClick,
            onSendClick = onSendClick,
            onMicClick = onMicClick,
            onPlayerClick = onPlayerClick
        )
    }
}