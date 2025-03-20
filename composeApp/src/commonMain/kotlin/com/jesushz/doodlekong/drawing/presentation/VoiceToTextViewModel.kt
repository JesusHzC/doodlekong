package com.jesushz.doodlekong.drawing.presentation

import androidx.lifecycle.ViewModel
import com.jesushz.doodlekong.drawing.data.models.DisplayState
import com.jesushz.doodlekong.drawing.data.models.VoiceToTextState
import com.jesushz.doodlekong.drawing.domain.voice_to_text.VoiceToTextParser
import com.jesushz.doodlekong.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val parser: VoiceToTextParser,
): ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(VoiceToTextState())
    val state = _state.combine(parser.state) { state, voiceResult ->
        state.copy(
            spokenText = voiceResult.result,
            displayState = when {
                voiceResult.error != null -> DisplayState.ERROR
                voiceResult.result.isNotBlank() && !voiceResult.isSpeaking -> {
                    DisplayState.DISPLAYING_RESULTS
                }
                voiceResult.isSpeaking -> DisplayState.SPEAKING
                else -> DisplayState.WAITING_TO_TALK
            }
        )
    }
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), VoiceToTextState())

    init {
        coroutineScope.launch {
            while (true) {
                if (state.value.displayState == DisplayState.SPEAKING) {
                    _state.update {
                        it.copy(
                            powerRatios = it.powerRatios + parser.state.value.powerRatio
                        )
                    }
                }
                delay(50L)
            }
        }
    }

    fun onEvent(event: VoiceToTextEvent) {
        when (event) {
            VoiceToTextEvent.Reset -> {
                parser.stopListening()
            }
            VoiceToTextEvent.StartRecording -> {
                if (state.value.displayState != DisplayState.SPEAKING) {
                    parser.startListening(Constants.DEFAULT_LANGUAGE_CODE)
                }
                else {
                    parser.stopListening()
                    parser.startListening(Constants.DEFAULT_LANGUAGE_CODE)
                }
            }
            else -> Unit
        }
    }

}
