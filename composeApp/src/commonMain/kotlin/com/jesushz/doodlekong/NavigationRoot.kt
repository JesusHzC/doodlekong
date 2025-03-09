package com.jesushz.doodlekong

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jesushz.doodlekong.setup.presentation.username.presentation.UsernameScreenRoot
import com.jesushz.doodlekong.util.Route

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Route = Route.SetupGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setupGraph(navController)
    }
}

private fun NavGraphBuilder.setupGraph(
    navController: NavHostController
) {
    navigation<Route.SetupGraph>(
        startDestination = Route.Username
    ) {
        composable<Route.Username> {
            UsernameScreenRoot()
        }
    }
}