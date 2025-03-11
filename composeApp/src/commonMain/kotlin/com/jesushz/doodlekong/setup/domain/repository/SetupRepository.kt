package com.jesushz.doodlekong.setup.domain.repository

import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.domain.DataError
import com.jesushz.doodlekong.core.domain.Result

interface SetupRepository {

    suspend fun createRoom(room: Room): Result<Unit, DataError>

    suspend fun getRooms(searchQuery: String): Result<List<Room>, DataError.Remote>

    suspend fun joinRoom(username: String, roomName: String): Result<Unit, DataError>

}
