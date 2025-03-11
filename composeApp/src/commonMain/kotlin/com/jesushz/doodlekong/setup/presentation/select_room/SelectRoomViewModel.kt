package com.jesushz.doodlekong.setup.presentation.select_room

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.jesushz.doodlekong.util.Route

class SelectRoomViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val username = savedStateHandle.toRoute<Route.SelectRoom>().username

}