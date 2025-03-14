package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jesushz.doodlekong.drawing.data.mappers.toColor
import com.jesushz.doodlekong.drawing.data.mappers.toDrawResource
import com.jesushz.doodlekong.drawing.data.models.DrawingActionType
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawingActions(
    modifier: Modifier = Modifier,
    onActionSelected: (DrawingActionType) -> Unit
) {
    var actionSelected by remember { mutableStateOf(DrawingActionType.BLACK_COLOR) }

    LaunchedEffect(key1 = actionSelected) {
        onActionSelected(actionSelected)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DrawingActionType.entries.forEach { action ->
            when (action) {
                DrawingActionType.UNDO -> {
                    action.toDrawResource()?.let {
                        IconButton(
                            onClick = {
                                onActionSelected(action)
                            }
                        ) {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = action.toString(),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
                DrawingActionType.ERASER -> {
                    action.toDrawResource(
                        isSelected = actionSelected == DrawingActionType.ERASER
                    )?.let {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    actionSelected = DrawingActionType.ERASER
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(it),
                                contentDescription = action.toString(),
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(
                                if (actionSelected == action) {
                                    45.dp
                                } else {
                                    40.dp
                                }
                            )
                            .clip(CircleShape)
                            .background(
                                color = action.toColor()
                            )
                            .then(
                                if (actionSelected == action) {
                                    Modifier
                                        .border(
                                            width = 2.dp,
                                            color = Color.Cyan,
                                            shape = CircleShape
                                        )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                actionSelected = action
                            }
                    )
                }
            }
        }
    }
}
