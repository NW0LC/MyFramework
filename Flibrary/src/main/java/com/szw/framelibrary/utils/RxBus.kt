package com.szw.framelibrary.utils

import com.hwangjr.rxbus.Bus

object RxBus {
    private var sBus: Bus? = null

    @Synchronized
    fun get(): Bus {
        if (sBus == null) {
            sBus = Bus()
        }
        return sBus as Bus
    }
}