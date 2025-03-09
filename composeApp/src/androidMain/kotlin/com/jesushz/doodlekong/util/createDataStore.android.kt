package com.jesushz.doodlekong.util

import android.content.Context

fun createDataStore(context: Context): PrefsDataStore {
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
