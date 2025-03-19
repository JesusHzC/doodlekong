package com.jesushz.doodlekong.drawing.presentation

import com.jesushz.doodlekong.core.data.network.ws.models.GameError

sealed interface DrawingEvent {

    data class OnGameError(val error: GameError): DrawingEvent
    data class OnConnectionError(val error: String): DrawingEvent

}
