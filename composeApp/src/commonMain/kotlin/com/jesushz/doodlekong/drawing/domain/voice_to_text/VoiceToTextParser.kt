package com.jesushz.doodlekong.drawing.domain.voice_to_text

import com.jesushz.doodlekong.drawing.data.voice_to_text.VoiceToTextParserState
import kotlinx.coroutines.flow.StateFlow

interface VoiceToTextParser {
    val state: StateFlow<VoiceToTextParserState>
    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}
