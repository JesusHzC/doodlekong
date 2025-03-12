package com.jesushz.doodlekong

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jesushz.doodlekong.drawing.presentation.DrawingScreenRoot
import com.jesushz.doodlekong.setup.presentation.create_room.CreateRoomScreenRoot
import com.jesushz.doodlekong.setup.presentation.select_room.SelectRoomScreenRoot
import com.jesushz.doodlekong.setup.presentation.username.UsernameScreenRoot
import com.jesushz.doodlekong.util.Route

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Route = Route.SetupGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        setupGraph(
            navController
        )
        drawingGraph(
            navController
        )
    }
}

private fun NavGraphBuilder.setupGraph(
    navController: NavHostController
) {
    navigation<Route.SetupGraph>(
        startDestination = Route.Username
    ) {
        composable<Route.Username> {
            UsernameScreenRoot(
                navigateToSelectRoomEvent = { username ->
                    navController.navigate(
                        Route.SelectRoom(username)
                    )
                }
            )
        }

        composable<Route.SelectRoom> {
            SelectRoomScreenRoot(
                onNavigateToCreateRoom = { username ->
                    navController.navigate(
                        Route.CreateRoom(username)
                    )
                },
                onNavigateToDrawingScreen = { username, roomName ->
                    navController.navigate(
                        Route.Drawing(username, roomName)
                    )
                }
            )
        }

        composable<Route.CreateRoom> {
            CreateRoomScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDrawingScreen = { username, roomName ->
                    navController.navigate(
                        Route.Drawing(username, roomName)
                    )
                }
            )
        }
    }
}

private fun NavGraphBuilder.drawingGraph(
    navController: NavHostController
) {
    navigation<Route.DrawingGraph>(
        startDestination = Route.Drawing(username = "", roomName = "")
    ) {
        composable<Route.Drawing> {
            DrawingScreenRoot()
        }
    }
}
