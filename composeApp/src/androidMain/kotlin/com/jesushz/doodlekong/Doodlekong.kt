package com.jesushz.doodlekong

import android.app.Application
import com.jesushz.doodlekong.di.initKoin
import org.koin.android.ext.koin.androidContext

class Doodlekong: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Doodlekong)
        }
    }

}
