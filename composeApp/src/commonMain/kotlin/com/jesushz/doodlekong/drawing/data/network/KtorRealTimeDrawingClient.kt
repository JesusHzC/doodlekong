package com.jesushz.doodlekong.drawing.data.network

import com.jesushz.doodlekong.core.data.network.NetworkConstants
import com.jesushz.doodlekong.core.data.network.ws.BaseModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json

class KtorRealTimeDrawingClient(
    private val client: HttpClient
): RealTimeDrawingClient {

    private var session: WebSocketSession? = null

    override fun getBaseModel(): Flow<BaseModel> {
        return flow {
            session = client.webSocketSession {
                url(NetworkConstants.SOCKET_URL)
            }
            val baseModels = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    Json.decodeFromString<BaseModel>(it.readText())
                }
            emitAll(baseModels)
        }
    }

    override suspend fun sendBaseModel(baseModel: BaseModel) {
        session?.outgoing?.send(
            Frame.Text(Json.encodeToString(baseModel))
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

}
