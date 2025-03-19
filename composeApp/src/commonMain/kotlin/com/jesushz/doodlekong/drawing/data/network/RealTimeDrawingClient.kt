package com.jesushz.doodlekong.drawing.data.network

import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RealTimeDrawingClient {

    fun getBaseModel(): Flow<BaseModel>
    suspend fun sendBaseModel(baseModel: BaseModel)
    suspend fun close()
    fun observeSession(): StateFlow<WebSocketSession?>

}
