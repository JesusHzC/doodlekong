package com.jesushz.doodlekong.util

import kotlinx.serialization.Serializable

sealed interface Route {

    // Graph
    @Serializable
    data object SetupGraph: Route

    @Serializable
    data object DrawingGraph: Route

    // Routes Setup Graph
    @Serializable
    data object Username: Route

    @Serializable
    data class SelectRoom(val username: String): Route

    @Serializable
    data class CreateRoom(val username: String): Route

    // Routes Drawing Graph
    @Serializable
    data class Drawing(val username: String, val roomName: String): Route

}
