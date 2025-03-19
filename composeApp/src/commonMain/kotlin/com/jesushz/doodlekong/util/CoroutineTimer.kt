package com.jesushz.doodlekong.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineTimer {

    fun timeAndEmit(
        duration: Long,
        coroutineScope: CoroutineScope,
        emissionFrequency: Long = 100L,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        onEmit: (Long) -> Unit
    ): Job {
        return coroutineScope.launch(dispatcher) {
            var time = duration
            while(time >= 0) {
                onEmit(time)
                time -= emissionFrequency
                delay(emissionFrequency)
            }
        }
    }

}
