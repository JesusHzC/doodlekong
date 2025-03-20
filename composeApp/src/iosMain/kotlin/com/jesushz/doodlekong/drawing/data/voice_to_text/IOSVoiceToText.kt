@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.jesushz.doodlekong.drawing.data.voice_to_text

import com.jesushz.doodlekong.drawing.domain.voice_to_text.VoiceToTextParser
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioInputNode
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.setActive
import platform.Foundation.NSLocale
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer
import platform.AVFAudio.AVAudioSessionCategoryOptionDuckOthers
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionModeSpokenAudio
import platform.Foundation.NSError
import platform.Speech.SFSpeechRecognizerAuthorizationStatus

class IOSVoiceToText: VoiceToTextParser {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    override val state: StateFlow<VoiceToTextParserState> get() = _state.asStateFlow()

    private val micObserver = MicrophonePowerObserver()
    private var micPowerJob: Job? = null

    private var recognizer: SFSpeechRecognizer? = null
    private var audioEngine: AVAudioEngine? = null
    private var inputNode: AVAudioInputNode? = null
    private var audioBufferRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private var audioSession: AVAudioSession? = null

    override fun startListening(languageCode: String) {
        updateState(error = null)

        setupRecognizer(languageCode) ?: return
        setupAudioSession()

        requestPermissions {
            setupRecognition()
        }
    }

    override fun stopListening() {
        updateState(isSpeaking = false)
        cleanupResources()
    }

    override fun reset() {
        stopListening()
        _state.value = VoiceToTextParserState()
    }

    override fun cancel() {
        // No es necesario en iOS
    }

    private fun setupRecognizer(languageCode: String): SFSpeechRecognizer? {
        val chosenLocale = NSLocale(languageCode)
        val supportedLocale = if (SFSpeechRecognizer.supportedLocales().contains(chosenLocale))
            chosenLocale else NSLocale("en-US")

        recognizer = SFSpeechRecognizer(locale = supportedLocale)
        if (recognizer?.available != true) {
            updateState(error = "Speech recognizer is not available")
            return null
        }
        return recognizer
    }

    private fun setupAudioSession() {
        audioSession = AVAudioSession.sharedInstance()
    }

    private fun requestPermissions(onGranted: () -> Unit) {
        audioSession?.requestRecordPermission { wasGranted ->
            if (!wasGranted) {
                updateState(error = "You need to grant permission to record your voice.")
                stopListening()
                return@requestRecordPermission
            }
            SFSpeechRecognizer.requestAuthorization { status ->
                if (status != SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                    updateState(error = "You need to grant permission to transcribe audio.")
                    stopListening()
                    return@requestAuthorization
                }
                onGranted()
            }
        }
    }

    private fun setupRecognition() {
        audioBufferRequest = SFSpeechAudioBufferRecognitionRequest()
        val request = audioBufferRequest ?: return

        recognitionTask = recognizer?.recognitionTaskWithRequest(request) { result, error ->
            if (result != null && result.isFinal()) {
                updateState(result = result.bestTranscription.formattedString)
            } else if (error != null) {
                updateState(error = error.localizedDescription)
            }
        }

        setupAudioEngine()
    }

    private fun setupAudioEngine() {
        audioEngine = AVAudioEngine()
        inputNode = audioEngine?.inputNode
        val recordingFormat = inputNode?.outputFormatForBus(0u)

        inputNode?.installTapOnBus(0u, bufferSize = 1024u, format = recordingFormat) { buffer, _ ->
            if (buffer != null) {
                audioBufferRequest?.appendAudioPCMBuffer(buffer)
            }
        }

        audioEngine?.prepare()
        startAudioProcessing()
    }

    private fun startAudioProcessing() {
        try {
            audioSession?.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                AVAudioSessionModeSpokenAudio,
                AVAudioSessionCategoryOptionDuckOthers,
                null
            )
            activateAudioSession()

            micObserver.startObserving()
            startAudioEngine()
            updateState(isSpeaking = true)

            micPowerJob = CoroutineScope(Dispatchers.Main).launch {
                micObserver.micPowerRatio.collect { ratio ->
                    updateState(powerRatio = ratio.toFloat())
                }
            }
        } catch (e: Exception) {
            updateState(error = e.message, isSpeaking = false)
        }
    }

    private fun cleanupResources() {
        micPowerJob?.cancel()
        micPowerJob = null

        micObserver.release()

        audioBufferRequest?.endAudio()
        audioBufferRequest = null

        audioEngine?.stop()
        inputNode?.removeTapOnBus(0u)

        try {
            activateAudioSession()
        } catch (_: Exception) {}

        audioSession = null
    }

    private fun updateState(result: String? = null, error: String? = null, powerRatio: Float? = null, isSpeaking: Boolean? = null) {
        _state.value = _state.value.copy(
            result = result ?: _state.value.result,
            error = error ?: _state.value.error,
            powerRatio = powerRatio ?: _state.value.powerRatio,
            isSpeaking = isSpeaking ?: _state.value.isSpeaking
        )
    }

    private fun activateAudioSession() {
        audioSession?.let { session ->
            memScoped {
                val errorVar = alloc<ObjCObjectVar<NSError?>>()
                val success = session.setActive(true, errorVar.ptr)
                if (!success) {
                    val errorMessage = errorVar.value?.localizedDescription ?: "Unknown error"
                    updateState(error = "Failed to activate audio session: $errorMessage")
                }
            }
        }
    }

    private fun startAudioEngine() {
        audioEngine?.let { engine ->
            memScoped {
                val errorVar = alloc<ObjCObjectVar<NSError?>>()
                val success = engine.startAndReturnError(errorVar.ptr)
                if (!success) {
                    val errorMessage = errorVar.value?.localizedDescription ?: "Unknown error"
                    updateState(error = "Failed to start audio engine: $errorMessage")
                }
            }
        }
    }
}
