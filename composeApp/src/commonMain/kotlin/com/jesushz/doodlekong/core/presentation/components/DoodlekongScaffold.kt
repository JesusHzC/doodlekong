package com.jesushz.doodlekong.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DoodlekongScaffold(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
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
