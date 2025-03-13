package com.jesushz.doodlekong.util

actual fun getSystemTime(): Long {
    return System.currentTimeMillis()
}