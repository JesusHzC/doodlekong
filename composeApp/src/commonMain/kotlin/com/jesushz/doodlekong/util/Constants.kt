package com.jesushz.doodlekong.util

object Constants {

    const val MIN_USERNAME_LENGTH = 4
    const val MAX_USERNAME_LENGTH = 12

    const val MIN_ROOM_NAME_LENGTH = 4
    const val MAX_ROOM_NAME_LENGTH = 16

    //Errors
    const val INPUT_EMPTY_ERROR = "The fields may not be empty"
    const val ERROR_USERNAME_TOO_SHORT = "The minimum username length is $MIN_USERNAME_LENGTH characters."
    const val ERROR_USERNAME_TOO_LONG = "The maximum username length is $MAX_USERNAME_LENGTH characters."
    const val ERROR_ROOM_NAME_TOO_SHORT = "The minimum username length is $MIN_ROOM_NAME_LENGTH characters."
    const val ERROR_ROOM_NAME_TOO_LONG = "The maximum username length is $MAX_ROOM_NAME_LENGTH characters."

    const val ERROR_DISK_FULL = "Oops, it seems like your disk is full."
    const val ERROR_UNKNOWN = "Oops, something went wrong."
    const val ERROR_REQUEST_TIMEOUT = "The request timed out."
    const val ERROR_NO_INTERNET = "Couldn't reach server, please check your internet connection."
    const val ERROR_TOO_MANY_REQUESTS = "Your quota seems to be exceeded."
    const val ERROR_SERIALIZATION = "Couldn't parse data."

    // Language code
    const val DEFAULT_LANGUAGE_CODE = "en"

}