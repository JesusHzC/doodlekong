package com.jesushz.doodlekong.setup.data.network

import com.jesushz.doodlekong.core.data.network.responses.BasicApiResponse
import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.domain.DataError
import com.jesushz.doodlekong.core.domain.Result

interface SetupRemoteDataSource {

    suspend fun createRoom(
        room: Room
    ): Result<BasicApiResponse, DataError.Remote>

    suspend fun getRooms(
        searchQuery: String
    ): Result<List<Room>, DataError.Remote>

    suspend fun joinRoom(
        username: String,
        roomName: String
    ): Result<BasicApiResponse, DataError.Remote>

}
