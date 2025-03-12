package com.jesushz.doodlekong.setup.presentation.create_room.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.jesushz.doodlekong.core.presentation.components.DoodleKongTextField
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.ic_arrow_drop_down
import doodlekong.composeapp.generated.resources.room_size
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun RoomSizeDropDown(
    modifier: Modifier = Modifier,
    onRoomSizeSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var sizeSelected by remember { mutableIntStateOf(2) }

    LaunchedEffect(key1 = sizeSelected) {
        onRoomSizeSelected(sizeSelected)
    }

    var maxWidth by remember {
        mutableIntStateOf(0)
    }
    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }

    Box {
        DoodleKongTextField(
            modifier = modifier
                .onSizeChanged {
                    maxWidth = max(maxWidth, it.width)
                }
                .clickable { expanded = !expanded },
            text = sizeSelected.toString(),
            label = stringResource(Res.string.room_size),
            onTextChanged = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_drop_down),
                    contentDescription = stringResource(Res.string.room_size),
                )
            },
            enabled = false,
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 24.sp
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .requiredWidth(maxWidthDp)
        ) {
            (2..8).forEach { size ->
                DropdownMenuItem(
                    onClick = {
                        sizeSelected = size
                        expanded = false
                    }
                ) {
                    Text(
                        text = size.toString(),
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}