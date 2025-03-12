package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.presentation.components.DoodleKongTextField
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_baseline_send_24
import doodlekong.composeapp.generated.resources.ic_mic
import doodlekong.composeapp.generated.resources.ic_mic_off
import doodlekong.composeapp.generated.resources.ic_person
import doodlekong.composeapp.generated.resources.ic_round_clear_24
import doodlekong.composeapp.generated.resources.mic
import doodlekong.composeapp.generated.resources.players
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatSection(
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
        Text(
            text = cutWord,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Column {
                IconButton(
                    onClick = {
                        onPlayerClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_person),
                        contentDescription = stringResource(Res.string.players)
                    )
                }
                IconButton(
                    onClick = {
                        onMicClick()
                    }
                ) {
                    Icon(
                        painter = if (micIsAvailable)
                            painterResource(Res.drawable.ic_mic)
                        else
                            painterResource(Res.drawable.ic_mic_off),
                        contentDescription = stringResource(Res.string.mic)
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(
                    items = chats,
                    key = { it.hashCode() }
                ) { chat ->

                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DoodleKongTextField(
                modifier = Modifier
                    .weight(1f),
                text = chatText,
                onTextChanged = {
                    onChatTextChanged(it)
                }
            )
            IconButton(
                onClick = {
                    onClearClick()
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_round_clear_24),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                    onSendClick()
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_baseline_send_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
internal fun ChatItem(
    modifier: Modifier = Modifier,
    chat: ChatMessage
) {

}
