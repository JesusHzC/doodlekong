package com.jesushz.doodlekong.setup.data.network

import com.jesushz.doodlekong.core.data.network.NetworkConstants.BASE_URL
import com.jesushz.doodlekong.core.data.network.NetworkConstants.CREATE_ROOM_ENDPOINT
import com.jesushz.doodlekong.core.data.network.NetworkConstants.GET_ROOMS_ENDPOINT
import com.jesushz.doodlekong.core.data.network.NetworkConstants.JOIN_ROOM_ENDPOINT
import com.jesushz.doodlekong.core.data.network.responses.BasicApiResponse
import com.jesushz.doodlekong.core.data.network.safeCall
import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.domain.DataError
import com.jesushz.doodlekong.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorSetupRemoteDataSource(
    private val client: HttpClient,
): SetupRemoteDataSource {

    override suspend fun createRoom(room: Room): Result<BasicApiResponse, DataError.Remote> {
        return safeCall<BasicApiResponse>{
            client.post(
                urlString = BASE_URL + CREATE_ROOM_ENDPOINT,
            ) {
                setBody(room)
            }
        }
    }

    override suspend fun getRooms(searchQuery: String): Result<List<Room>, DataError.Remote> {
        return safeCall<List<Room>>{
            client.get(
                urlString = BASE_URL + GET_ROOMS_ENDPOINT,
            ) {
                parameter("searchQuery", searchQuery)
            }
        }
    }

    override suspend fun joinRoom(
        username: String,
        roomName: String
    ): Result<BasicApiResponse, DataError.Remote> {
        return safeCall<BasicApiResponse>{
            client.get(
                urlString = BASE_URL + JOIN_ROOM_ENDPOINT,
            ) {
                parameter("username", username)
                parameter("roomName", roomName)
            }
        }
    }

}
