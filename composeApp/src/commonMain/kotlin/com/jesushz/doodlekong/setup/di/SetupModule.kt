package com.jesushz.doodlekong.setup.di

import com.jesushz.doodlekong.setup.data.network.KtorSetupRemoteDataSource
import com.jesushz.doodlekong.setup.data.network.SetupRemoteDataSource
import com.jesushz.doodlekong.setup.data.repository.DefaultSetupRepository
import com.jesushz.doodlekong.setup.domain.repository.SetupRepository
import com.jesushz.doodlekong.setup.presentation.create_room.CreateRoomViewModel
import com.jesushz.doodlekong.setup.presentation.select_room.SelectRoomViewModel
import com.jesushz.doodlekong.setup.presentation.username.UsernameViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val setupModule = module {
    singleOf(::KtorSetupRemoteDataSource).bind<SetupRemoteDataSource>()
    singleOf(::DefaultSetupRepository).bind<SetupRepository>()

    viewModelOf(::UsernameViewModel)
    viewModelOf(::SelectRoomViewModel)
    viewModelOf(::CreateRoomViewModel)
}