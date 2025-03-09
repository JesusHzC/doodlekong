package com.jesushz.doodlekong.setup.data.repository

import com.jesushz.doodlekong.core.data.network.ws.Room
import com.jesushz.doodlekong.core.domain.DataError
import com.jesushz.doodlekong.core.domain.Result
import com.jesushz.doodlekong.core.domain.asEmptyDataResult
import com.jesushz.doodlekong.setup.data.network.SetupRemoteDataSource
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository

class DefaultSetupRepository(
    private val setupRemoteDataSource: SetupRemoteDataSource
): SetupRepository {

    override suspend fun createRoom(room: Room): Result<Unit, DataError.Remote> {
        return setupRemoteDataSource
            .createRoom(room)
            .asEmptyDataResult()
    }

    override suspend fun getRooms(searchQuery: String): Result<List<Room>, DataError.Remote> {
        return setupRemoteDataSource
            .getRooms(searchQuery)
    }

    override suspend fun joinRoom(
        username: String,
        roomName: String
    ): Result<Unit, DataError.Remote> {
        return setupRemoteDataSource
            .joinRoom(username, roomName)
            .asEmptyDataResult()
    }

}
