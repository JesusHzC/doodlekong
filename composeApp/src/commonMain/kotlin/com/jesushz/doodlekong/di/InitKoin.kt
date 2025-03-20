package com.jesushz.doodlekong.di

import com.jesushz.doodlekong.drawing.data.voice_to_text.voiceToTextModule
import com.jesushz.doodlekong.drawing.di.drawingModule
import com.jesushz.doodlekong.setup.di.setupModule
import com.jesushz.doodlekong.util.preferencesModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            platformModule,
            setupModule,
            drawingModule,
            preferencesModule,
            voiceToTextModule
        )
    }
}
