@file:OptIn(ExperimentalForeignApi::class)

package com.jesushz.doodlekong.drawing.data.voice_to_text

import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.CoreAudioTypes.kAudioFormatAppleLossless
import platform.Foundation.NSURL
import kotlinx.coroutines.*

class MicrophonePowerObserver {

    private val _micPowerRatio = MutableStateFlow(0.0)
    val micPowerRatio: StateFlow<Double> get() = _micPowerRatio

    private var audioRecorder: AVAudioRecorder? = null
    private var job: Job? = null
    private val powerRatioEmissionsPerSecond = 20.0

    fun startObserving() {
        release()

        val settings = mapOf<Any?, Any?>(
            AVFormatIDKey to kAudioFormatAppleLossless,
            AVNumberOfChannelsKey to 1
        )

        val url = NSURL.fileURLWithPath("/dev/null")
        val recorder = AVAudioRecorder(url, settings, null)

        if (recorder != null) {
            recorder.setMeteringEnabled(true)
            recorder.record()
            audioRecorder = recorder

            job = CoroutineScope(Dispatchers.Main).launch {
                while (isActive) {
                    recorder.updateMeters()
                    val powerOffset = recorder.averagePowerForChannel(0u)
                    _micPowerRatio.value = if (powerOffset < -50) {
                        0.0
                    } else {
                        (50 + powerOffset) / 50.0
                    }
                    delay((1000 / powerRatioEmissionsPerSecond).toLong())
                }
            }
        } else {
            Logger.e("Failed to start observing microphone power", tag = "MicrophonePowerObserver")
        }
    }

    fun release() {
        job?.cancel()
        job = null
        audioRecorder?.stop()
        audioRecorder = null
        _micPowerRatio.value = 0.0
    }
}
