package com.jesushz.doodlekong.drawing.data.voice_to_text

import com.jesushz.doodlekong.drawing.domain.voice_to_text.VoiceToTextParser
import org.koin.core.module.Module
import org.koin.dsl.module

actual val voiceToTextModule: Module
    get() = module {
        single<VoiceToTextParser> {
            IOSVoiceToText()
        }
    }
