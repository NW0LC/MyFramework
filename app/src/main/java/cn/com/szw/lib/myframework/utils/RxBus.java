package cn.com.szw.lib.myframework.utils;

import com.hwangjr.rxbus.Bus;

public final class RxBus {
    private static Bus sBus;

    public synchronized static Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}