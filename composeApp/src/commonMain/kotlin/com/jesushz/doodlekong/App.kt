package com.jesushz.doodlekong

import androidx.compose.material.Typography
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.rememberNavController
import doodlekong.composeapp.generated.resources.Res
import doodlekong.composeapp.generated.resources.handwriting
import org.jetbrains.compose.resources.Font

val Typography
    @Composable get() = Typography(
        defaultFontFamily = FontFamily(Font(Res.font.handwriting))
    )

@Composable
fun App() {
    MaterialTheme(
        typography = Typography
    ) {
        val navController = rememberNavController()

        NavigationRoot(
            navController = navController,
        )
    }
}
