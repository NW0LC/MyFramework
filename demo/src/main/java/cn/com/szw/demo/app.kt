package cn.com.szw.demo


import android.util.Log
import com.szw.framelibrary.app.MyApplication
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent



class app : MyApplication() {
    override fun getSaltStr(): String? = ""
    override fun onCreate() {
        super.onCreate()
        init()
        val mPushAgent = PushAgent.getInstance(this)
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {

            override fun onSuccess(deviceToken: String) {
                //注册成功会返回device token
                Log.i("deviceToken",deviceToken)
            }

            override fun onFailure(s: String, s1: String) {

            }
        })
    }
}
