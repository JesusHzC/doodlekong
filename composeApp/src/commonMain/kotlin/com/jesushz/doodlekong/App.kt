package com.jesushz.doodlekong

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavigationRoot(
            navController = navController
        )
    }
}