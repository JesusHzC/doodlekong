package com.jesushz.doodlekong.drawing.di

import com.jesushz.doodlekong.drawing.presentation.DrawingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val drawingModule = module {
    viewModelOf(::DrawingViewModel)
}
