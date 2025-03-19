@file:OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)

package com.jesushz.doodlekong.drawing.data.network

import co.touchlab.kermit.Logger
import com.jesushz.doodlekong.core.data.network.NetworkConstants
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_ANNOUNCEMENT
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CHAT_MESSAGE
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CHOSEN_WORD
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_CUR_ROUND_DRAW_INFO
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DISCONNECT_REQUEST
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_ACTION
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_DRAW_DATA
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_GAME_ERROR
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_GAME_STATE
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_JOIN_ROOM_HANDSHAKE
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_NEW_WORDS
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PHASE_CHANGE
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PING
import com.jesushz.doodlekong.core.data.network.NetworkConstants.TYPE_PLAYERS_LIST
import com.jesushz.doodlekong.core.data.network.ws.models.Announcement
import com.jesushz.doodlekong.core.data.network.ws.models.BaseModel
import com.jesushz.doodlekong.core.data.network.ws.models.ChatMessage
import com.jesushz.doodlekong.core.data.network.ws.models.ChosenWord
import com.jesushz.doodlekong.core.data.network.ws.models.DisconnectRequest
import com.jesushz.doodlekong.core.data.network.ws.models.DrawAction
import com.jesushz.doodlekong.core.data.network.ws.models.DrawData
import com.jesushz.doodlekong.core.data.network.ws.models.GameError
import com.jesushz.doodlekong.core.data.network.ws.models.GameState
import com.jesushz.doodlekong.core.data.network.ws.models.JoinRoomHandshake
import com.jesushz.doodlekong.core.data.network.ws.models.NewWords
import com.jesushz.doodlekong.core.data.network.ws.models.PhaseChange
import com.jesushz.doodlekong.core.data.network.ws.models.Ping
import com.jesushz.doodlekong.core.data.network.ws.models.PlayersList
import com.jesushz.doodlekong.core.data.network.ws.models.RoundDrawInfo
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

class KtorRealTimeDrawingClient(
    private val client: HttpClient
): RealTimeDrawingClient {

    private var session: WebSocketSession? = null

    private val format by lazy {
        val module = SerializersModule {
            polymorphic(BaseModel::class) {
                subclass(Announcement::class)
                subclass(ChatMessage::class)
                subclass(ChosenWord::class)
                subclass(DisconnectRequest::class)
                subclass(DrawAction::class)
                subclass(DrawData::class)
                subclass(GameError::class)
                subclass(GameState::class)
                subclass(JoinRoomHandshake::class)
                subclass(NewWords::class)
                subclass(PhaseChange::class)
                subclass(Ping::class)
                subclass(PlayersList::class)
                subclass(RoundDrawInfo::class)
            }
        }
        val format = Json {
            serializersModule = module
            encodeDefaults = true
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
        format
    }

    override fun getBaseModel(
        onConnectionError: (Throwable) -> Unit,
        onConnectionSuccess: () -> Unit
    ): Flow<BaseModel> {
        return flow {
            session = client.webSocketSession {
                url(NetworkConstants.SOCKET_URL)
            }
            if (session == null) {
                onConnectionError(Throwable("Couldn't establish a connection."))
            } else {
                onConnectionSuccess()
            }
            val baseModels = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    val type = Json.decodeFromString<Map<String, JsonElement>>(it.readText())["type"]
                    val clazz = when (type) {
                        null -> {
                            BaseModel::class
                        }
                        is JsonPrimitive -> {
                            when (type.content) {
                                TYPE_CHAT_MESSAGE -> ChatMessage::class
                                TYPE_DRAW_DATA -> DrawData::class
                                TYPE_ANNOUNCEMENT -> Announcement::class
                                TYPE_JOIN_ROOM_HANDSHAKE -> JoinRoomHandshake::class
                                TYPE_PHASE_CHANGE -> PhaseChange::class
                                TYPE_CHOSEN_WORD -> ChosenWord::class
                                TYPE_GAME_STATE -> GameState::class
                                TYPE_PING -> Ping::class
                                TYPE_DISCONNECT_REQUEST -> DisconnectRequest::class
                                TYPE_DRAW_ACTION -> DrawAction::class
                                TYPE_PLAYERS_LIST -> PlayersList::class
                                TYPE_NEW_WORDS -> NewWords::class
                                TYPE_GAME_ERROR -> GameError::class
                                TYPE_CUR_ROUND_DRAW_INFO -> RoundDrawInfo::class
                                else -> BaseModel::class
                            }
                        }
                        else -> {
                            BaseModel::class
                        }
                    }
                    format.decodeFromJsonElement(clazz.serializer(), Json.parseToJsonElement(it.readText()))
                }
            emitAll(baseModels)
        }
    }

    override suspend fun sendBaseModel(baseModel: BaseModel) {
        session?.let {
            Logger.i("sendBaseModel: ${format.encodeToString(baseModel)}", tag = WS_TAG)
            it.outgoing.send(
                Frame.Text(format.encodeToString(baseModel))
            )
        }
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

    companion object {
        const val WS_TAG = "WebSocket"
    }

}
