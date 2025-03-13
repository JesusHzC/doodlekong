package com.jesushz.doodlekong.util

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun getSystemTime(): Long {
    return (NSDate().timeIntervalSince1970() * 1000).toLong()
}
