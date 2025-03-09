package com.jesushz.doodlekong

import androidx.compose.ui.window.ComposeUIViewController
import com.jesushz.doodlekong.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }