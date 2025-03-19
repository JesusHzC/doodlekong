@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.doodlekong.di

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jesushz.doodlekong.core.data.network.HttpClientFactory
import com.jesushz.doodlekong.util.DispatcherProvider
import com.jesushz.doodlekong.util.PrefsDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect val platformModule: Module

val sharedModule = module {

    single<DispatcherProvider> {
        object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }

    single { HttpClientFactory.create(get(), get()) }

    // Provide client Id from data store
    single<String> {
        val dataStore = get<PrefsDataStore>()
        val clientIdKey = stringPreferencesKey("clientId")
        val preferences = runBlocking { dataStore.data.first() }
        val clientIdExists = preferences[clientIdKey] != null
        if (clientIdExists) {
            preferences[clientIdKey] ?: ""
        } else {
            val newClientId = Uuid.random().toString()
            runBlocking {
                dataStore.edit{ settings ->
                    settings[clientIdKey] = newClientId
                }
            }
            newClientId
        }
    }

}
