package com.jesushz.doodlekong.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.back
import doodlekong.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DoodleKongScaffold(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    snackBarHostState: SnackbarHostState? = null,
    onBackClicked: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (showBackButton) {
                TopBarWithBackButton(
                    onBackClicked = onBackClicked
                )
            }
        },
        snackbarHost = {
            snackBarHostState?.let { state ->
                SnackbarHost(
                    hostState = state,
                    snackbar = { data ->
                        Snackbar(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(
                                text = data.message,
                                style = LocalTextStyle.current.copy(
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                )
            }
        },
        modifier = modifier
            .systemBarsPadding()
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
internal fun TopBarWithBackButton(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {},
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.back),
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    )
}
