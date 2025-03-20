package com.jesushz.doodlekong.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesushz.doodlekong.core.data.network.ws.models.PlayerData
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_pencil
import org.jetbrains.compose.resources.painterResource

@Composable
fun DoodleKongNavigation(
    modifier: Modifier = Modifier,
    players: List<PlayerData>
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = players,
            key = { it.hashCode() }
        ) { player ->
            PlayerItem(
                modifier = Modifier
                    .fillMaxWidth(),
                player = player
            )
        }
    }
}

@Composable
internal fun PlayerItem(
    modifier: Modifier = Modifier,
    player: PlayerData
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = player.rank.toString(),
            fontSize = 40.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = player.username,
            fontSize = 30.sp,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(Res.drawable.ic_pencil),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = player.score.toString(),
            fontSize = 40.sp,
            modifier = Modifier
                .weight(0.3f)
        )
    }
}
