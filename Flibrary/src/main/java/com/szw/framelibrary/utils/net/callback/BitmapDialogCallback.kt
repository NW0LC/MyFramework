package com.szw.framelibrary.utils.net.callback

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast

import com.lzy.okgo.callback.BitmapCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.szw.framelibrary.view.CustomProgress


/**
 * ================================================
 * 描    述：请求图图片的时候显示对话框
 * ================================================
 */
abstract class BitmapDialogCallback(private val context: Context) : BitmapCallback() {
    override fun onStart(request: Request<Bitmap, out Request<*, *>>?) {
        super.onStart(request)
        CustomProgress.show(context, "加载中", false, null)
    }

    override fun onFinish() {
        CustomProgress.disMiss()
    }

    override fun onError(response: Response<Bitmap>) {
        super.onError(response)
        val sb: StringBuilder
        val call = response.rawCall
        if (call != null) {
            Toast.makeText(context, "请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url(), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}