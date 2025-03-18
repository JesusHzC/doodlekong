@file:OptIn(ExperimentalLayoutApi::class)

package com.jesushz.doodlekong.drawing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_baseline_undo_24
import doodlekong.composeapp.generated.resources.undo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun DrawingControls(
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Black,
    colors: List<Color> = emptyList(),
    onSelectedColor: (Color) -> Unit,
    onClearCanvas: () -> Unit,
) {
    var maxWidth by remember {
        mutableIntStateOf(0)
    }
    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }
    var maxHeight by remember {
        mutableIntStateOf(0)
    }
    val maxHeightDp = with(LocalDensity.current) { maxHeight.toDp() }
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.fastForEach { color ->
            val isSelected = selectedColor == color
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val scale = if (isSelected) 1.4f else 1f
                        scaleX = scale
                        scaleY = scale
                    }
                    .weight(0.5f)
                    .requiredSizeIn(
                        maxWidth = 40.dp,
                        maxHeight = 40.dp,
                        minWidth = 25.dp,
                        minHeight = 25.dp
                    )
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) {
                            if (color == Color.Black) {
                                Color.Cyan
                            } else {
                                Color.Black
                            }
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .onSizeChanged { size ->
                        maxWidth = max(maxWidth, size.width)
                        maxHeight = max(maxHeight, size.height)
                    }
                    .clickable {
                        onSelectedColor(color)
                    }
            )
        }
        IconButton(
            onClick = {
                onClearCanvas()
            },
            modifier = Modifier
                .weight(0.5f)
                .size(
                    width = maxWidthDp,
                    height = maxHeightDp
                )
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_baseline_undo_24),
                contentDescription = stringResource(Res.string.undo)
            )
        }
        Spacer(modifier = Modifier.weight(2f))
    }
}