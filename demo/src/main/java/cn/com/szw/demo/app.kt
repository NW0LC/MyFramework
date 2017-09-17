package cn.com.szw.demo


import com.szw.framelibrary.app.MyApplication

class app : MyApplication() {
    override fun getSaltStr(): String? = ""
    override fun onCreate() {
        super.onCreate()
        init()
    }
}
