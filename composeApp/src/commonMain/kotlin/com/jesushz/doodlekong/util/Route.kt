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
    data object CreateRoom: Route

}
