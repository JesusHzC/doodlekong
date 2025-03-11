@file:OptIn(ExperimentalMaterialApi::class)

package com.jesushz.doodlekong.setup.presentation.select_room.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesushz.doodlekong.core.data.network.ws.Room

@Composable
fun RoomsList(
    modifier: Modifier = Modifier,
    rooms: List<Room>,
    onRoomClick: (Room) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = rooms,
            key = { it.name }
        ) { room ->
            RoomItem(
                room = room,
                onRoomClick = { onRoomClick(room) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun RoomItem(
    modifier: Modifier = Modifier,
    room: Room,
    onRoomClick: () -> Unit
) {
    Card(
        onClick = onRoomClick,
        border = BorderStroke(
            width = 4.dp,
            color = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = room.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "${room.playerCount}/ ${room.maxPlayers}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
