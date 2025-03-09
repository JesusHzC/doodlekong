package com.jesushz.doodlekong.di

import com.jesushz.doodlekong.core.data.network.HttpClientFactory
import com.jesushz.doodlekong.setup.data.network.KtorSetupRemoteDataSource
import com.jesushz.doodlekong.setup.data.network.SetupRemoteDataSource
import com.jesushz.doodlekong.setup.data.repository.DefaultSetupRepository
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository
import com.jesushz.doodlekong.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

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

    single { HttpClientFactory.create(get()) }

    singleOf(::KtorSetupRemoteDataSource).bind<SetupRemoteDataSource>()
    singleOf(::DefaultSetupRepository).bind<SetupRepository>()

}
