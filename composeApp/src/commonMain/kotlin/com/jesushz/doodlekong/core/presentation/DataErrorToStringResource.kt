package com.jesushz.doodlekong.core.presentation

import com.jesushz.doodlekong.core.domain.DataError
import com.jesushz.doodlekong.util.Constants

fun DataError.toUiText(): UiText {
    val string = when(this) {
        DataError.Local.DISK_FULL -> Constants.ERROR_DISK_FULL
        DataError.Local.UNKNOWN -> Constants.ERROR_UNKNOWN
        DataError.Remote.REQUEST_TIMEOUT -> Constants.ERROR_REQUEST_TIMEOUT
        DataError.Remote.TOO_MANY_REQUESTS -> Constants.ERROR_TOO_MANY_REQUESTS
        DataError.Remote.NO_INTERNET -> Constants.ERROR_NO_INTERNET
        DataError.Remote.SERVER -> Constants.ERROR_UNKNOWN
        DataError.Remote.SERIALIZATION -> Constants.ERROR_SERIALIZATION
        DataError.Remote.UNKNOWN -> Constants.ERROR_UNKNOWN
        is DataError.ApiError -> this.message
    }

    return UiText.DynamicString(string)
}
