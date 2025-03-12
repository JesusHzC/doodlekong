package com.jesushz.doodlekong.drawing.di

import com.jesushz.doodlekong.drawing.data.network.KtorRealTimeDrawingClient
import com.jesushz.doodlekong.drawing.data.network.RealTimeDrawingClient
import com.jesushz.doodlekong.drawing.presentation.DrawingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val drawingModule = module {
    singleOf(::KtorRealTimeDrawingClient).bind<RealTimeDrawingClient>()

    viewModelOf(::DrawingViewModel)
}
