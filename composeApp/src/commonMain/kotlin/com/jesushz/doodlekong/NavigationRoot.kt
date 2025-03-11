package com.jesushz.doodlekong

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jesushz.doodlekong.setup.presentation.select_room.SelectRoomScreenRoot
import com.jesushz.doodlekong.setup.presentation.username.UsernameScreenRoot
import com.jesushz.doodlekong.util.Route

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Route = Route.SetupGraph,
    snackBarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        setupGraph(
            navController,
            snackBarHostState
        )
    }
}

private fun NavGraphBuilder.setupGraph(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState
) {
    navigation<Route.SetupGraph>(
        startDestination = Route.Username
    ) {
        composable<Route.Username> {
            UsernameScreenRoot(
                snackBarHostState = snackBarHostState,
                navigateToSelectRoomEvent = { username ->
                    navController.navigate(
                        Route.SelectRoom(username)
                    )
                }
            )
        }

        composable<Route.SelectRoom> {
            SelectRoomScreenRoot()
        }
    }
}