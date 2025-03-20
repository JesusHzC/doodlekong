package com.jesushz.doodlekong.drawing.data.models

data class VoiceToTextState(
    val powerRatios: List<Float> = emptyList(),
    val spokenText: String = "",
    val recordError: String? = null,
    val displayState: DisplayState? = null,
)

enum class DisplayState {
    WAITING_TO_TALK,
    SPEAKING,
    DISPLAYING_RESULTS,
    ERROR
}
