package com.jesushz.doodlekong.drawing.data.network

import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import kotlinx.coroutines.flow.Flow

interface RealTimeDrawingClient {

    fun getBaseModel(
        onConnectionError: (Throwable) -> Unit,
        onConnectionSuccess: () -> Unit
    ): Flow<BaseModel>
    suspend fun sendBaseModel(baseModel: BaseModel)
    suspend fun close()

}
