package com.jesushz.doodlekong.drawing.data.voice_to_text

data class VoiceToTextParserState(
    val result: String = "",
    val error: String? = null,
    val powerRatio: Float = 0f,
    val isSpeaking: Boolean = false,
)
