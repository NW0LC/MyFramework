package com.szw.framelibrary.utils.net.callback

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.view.CustomProgress

import java.net.UnknownHostException



/**
 * ================================================
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
abstract class DialogCallback<T : NetEntity<*>>( val context: Context) : JsonCallback<T>() {

    override fun onStart(request: Request<T, out Request<*, *>>?) {
        super.onStart(request)
        CustomProgress.show(context, "加载中", false, null)
    }

    override fun onFinish() {
        CustomProgress.disMiss()
    }

    override fun onError(response: Response<T>) {
        super.onError(response)
        CustomProgress.disMiss()
        val sb: StringBuilder
        val call = response.rawCall
        if (call != null) {
            val e = response.exception
            if (e != null) {
                if (e is UnknownHostException) {
                    Toast.makeText(context, "网络未连接，请打开网络后再次尝试。", Toast.LENGTH_SHORT).show()
                }
            }
            Log.e("OkGo", "请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url())
        }
        val rawResponse = response.rawResponse
        if (rawResponse != null) {
            val responseHeadersString = rawResponse.headers()
            val names = responseHeadersString.names()
            sb = StringBuilder()
            sb.append("stateCode ： ").append(rawResponse.code()).append("\n")
            for (name in names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n")
            }
            Log.e("OkGo", sb.toString())
        }
    }
}