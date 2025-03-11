package com.jesushz.doodlekong

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Typography
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        val snackBarHostState = remember {
            SnackbarHostState()
        }
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
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
            },
            modifier = Modifier
                .navigationBarsPadding()
        ) {
            NavigationRoot(
                navController = navController,
                snackBarHostState = snackBarHostState
            )
        }
    }
}
