package com.jesushz.doodlekong.drawing.presentation

sealed interface VoiceToTextEvent {

    data object Reset: VoiceToTextEvent
    data object Close: VoiceToTextEvent
    data object StartRecording: VoiceToTextEvent

}
