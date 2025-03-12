package com.jesushz.doodlekong.setup.di

import com.jesushz.doodlekong.setup.presentation.create_room.CreateRoomViewModel
import com.jesushz.doodlekong.setup.presentation.select_room.SelectRoomViewModel
import com.jesushz.doodlekong.setup.presentation.username.UsernameViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupModule = module {
    viewModelOf(::UsernameViewModel)
    viewModelOf(::SelectRoomViewModel)
    viewModelOf(::CreateRoomViewModel)
}