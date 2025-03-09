package com.jesushz.doodlekong

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform