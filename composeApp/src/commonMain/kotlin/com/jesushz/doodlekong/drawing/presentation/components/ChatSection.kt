package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesushz.doodlekong.core.data.network.ws.models.Announcement
import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.presentation.components.DoodleKongTextField
import com.jesushz.doodlekong.util.BubbleType
import com.jesushz.doodlekong.util.dateFormat
import com.jesushz.doodlekong.util.speechBubble
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_baseline_send_24
import doodlekong.composeapp.generated.resources.ic_mic
import doodlekong.composeapp.generated.resources.ic_mic_off
import doodlekong.composeapp.generated.resources.ic_person
import doodlekong.composeapp.generated.resources.ic_round_clear_24
import doodlekong.composeapp.generated.resources.message
import doodlekong.composeapp.generated.resources.mic
import doodlekong.composeapp.generated.resources.players
import doodlekong.composeapp.generated.resources.username
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatSection(
    modifier: Modifier = Modifier,
    username: String,
    cutWord: String?,
    chatText: String,
    micIsAvailable: Boolean,
    showMic: Boolean = false,
    showSendChatMessage: Boolean = false,
    chatObjects: List<BaseModel>,
    onChatTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit,
    onPlayerClick: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier
    ) {
        cutWord?.let { text ->
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        }
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
                if (showMic) {
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
            }
            Spacer(modifier = Modifier.width(4.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = rememberLazyListState(
                    initialFirstVisibleItemIndex = chatObjects.size,
                    initialFirstVisibleItemScrollOffset = 0
                )
            ) {
                items(
                    items = chatObjects,
                    key = { it.hashCode() }
                ) { chatObject ->
                    when (chatObject) {
                        is Announcement -> {
                            AnnouncementItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                announcement = chatObject
                            )
                        }
                        is ChatMessage -> {
                            if (username == chatObject.from) {
                                ChatItemOutgoing(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    chat = chatObject
                                )
                            } else {
                                ChatItemIncoming(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    chat = chatObject
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showSendChatMessage) {
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
                        keyboard?.hide()
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
}

@Composable
internal fun ChatItemIncoming(
    modifier: Modifier = Modifier,
    chat: ChatMessage
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .speechBubble(
                    bubbleType = BubbleType.INCOMING
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = 15.dp,
                        end = 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    text = chat.from,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = chat.message,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = dateFormat(chat.timestamp),
            fontSize = 24.sp,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
internal fun ChatItemOutgoing(
    modifier: Modifier = Modifier,
    chat: ChatMessage
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = dateFormat(chat.timestamp),
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .speechBubble(
                        bubbleType = BubbleType.OUTGOING
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 15.dp,
                            end = 8.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                ) {
                    Text(
                        text = chat.from,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = chat.message,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
internal fun AnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement
) {
    Box(
        modifier = modifier
            .background(Color.Yellow)
            .clip(RectangleShape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = announcement.message,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = dateFormat(announcement.timestamp),
                fontSize = 20.sp,
            )
        }
    }
}
