package com.jesushz.doodlekong.util

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

fun createDataStore(context: Context): PrefsDataStore {
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}

actual val preferencesModule: Module
    get() = module {
        single {
            createDataStore(androidContext())
        }
    }
