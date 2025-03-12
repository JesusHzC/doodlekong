package com.jesushz.doodlekong.core.data.network

object NetworkConstants {

    // Base URL of the server
    const val BASE_URL = "http://192.168.1.6:8080"
    // Socket URL of the server
    const val SOCKET_HOST = "192.168.1.6"
    const val SOCKET_ENDPOINT = "/ws/draw"
    const val SOCKET_PORT = 8080

    // Endpoints of the server
    const val CREATE_ROOM_ENDPOINT = "/api/createRoom"
    const val GET_ROOMS_ENDPOINT = "/api/getRooms"
    const val JOIN_ROOM_ENDPOINT = "/api/joinRoom"

    // Type Model from Socket Response
    const val TYPE_CHAT_MESSAGE = "TYPE_CHAT_MESSAGE"
    const val TYPE_DRAW_DATA = "TYPE_DRAW_DATA"
    const val TYPE_ANNOUNCEMENT = "TYPE_ANNOUNCEMENT"
    const val TYPE_JOIN_ROOM_HANDSHAKE = "TYPE_JOIN_ROOM_HANDSHAKE"
    const val TYPE_GAME_ERROR = "TYPE_GAME_ERROR"
    const val TYPE_PHASE_CHANGE = "TYPE_PHASE_CHANGE"
    const val TYPE_CHOSEN_WORD = "TYPE_CHOSEN_WORD"
    const val TYPE_GAME_STATE = "TYPE_GAME_STATE"
    const val TYPE_NEW_WORDS = "TYPE_NEW_WORDS"
    const val TYPE_PLAYERS_LIST = "TYPE_PLAYERS_LIST"
    const val TYPE_PING = "TYPE_PING"
    const val TYPE_DISCONNECT_REQUEST = "TYPE_DISCONNECT_REQUEST"
    const val TYPE_DRAW_ACTION = "TYPE_DRAW_ACTION"
    const val TYPE_CUR_ROUND_DRAW_INFO = "TYPE_CUR_ROUND_DRAW_INFO"

}
