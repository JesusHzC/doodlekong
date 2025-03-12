package com.jesushz.doodlekong.setup.presentation.username

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.doodlekong.core.presentation.ObserveAsEvents
import com.jesushz.doodlekong.core.presentation.components.DoodlekongScaffold
import com.jesushz.doodlekong.setup.presentation.components.DoodleKongTextField
import com.jesushz.doodlekong.setup.presentation.username.components.NextButton
import com.jesushz.doodlekong.util.Constants
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.choose_a_username
import doodlekong.composeapp.generated.resources.doodlekong
import doodlekong.composeapp.generated.resources.ic_appicon_v2
import doodlekong.composeapp.generated.resources.next
import doodlekong.composeapp.generated.resources.username
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UsernameScreenRoot(
    viewModel: UsernameViewModel = koinViewModel(),
    navigateToSelectRoomEvent: (username: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            UsernameEvent.InputEmptyError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.INPUT_EMPTY_ERROR
                    )
                }
            }
            UsernameEvent.InputTooLongError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.ERROR_USERNAME_TOO_LONG
                    )
                }
            }
            UsernameEvent.InputTooShortError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = Constants.ERROR_USERNAME_TOO_SHORT
                    )
                }
            }
            is UsernameEvent.NavigateToSelectRoomEvent -> {
                navigateToSelectRoomEvent(event.username)
            }
        }
    }
    UsernameScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun UsernameScreen(
    state: UsernameState,
    snackBarHostState: SnackbarHostState,
    onAction: (UsernameAction) -> Unit
) {
    DoodlekongScaffold(
        snackBarHostState = snackBarHostState
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painterResource(resource = Res.drawable.ic_appicon_v2),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(Res.string.doodlekong),
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = stringResource(Res.string.choose_a_username),
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                DoodleKongTextField(
                    text = state.username,
                    label = stringResource(Res.string.username),
                    modifier = Modifier
                        .fillMaxWidth(),
                    onTextChanged = {
                        onAction(UsernameAction.OnUsernameChanged(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                NextButton(
                    text = stringResource(Res.string.next),
                    modifier = Modifier
                        .align(Alignment.End),
                    onButtonClicked = {
                        onAction(UsernameAction.OnNextClicked)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
