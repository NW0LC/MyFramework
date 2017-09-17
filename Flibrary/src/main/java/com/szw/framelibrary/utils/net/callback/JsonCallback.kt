package com.szw.framelibrary.utils.net.callback


import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.request.base.Request
import com.szw.framelibrary.utils.net.NetEntity
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * ================================================
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * ================================================
 */
abstract class JsonCallback<T:NetEntity<*>> : AbsCallback<T> {
    private var type: Type? = null
    private var clazz: Class<T>?=null

    constructor()

    constructor(type: Type) {
        this.type = type
    }

    constructor(clazz: Class<T>) {
        this.clazz = clazz
    }

    override fun onStart(request: Request<T, out Request<*, *>>?) {
        super.onStart(request)
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        //        request.headers("header1", "HeaderValue1")//
        //                .params("params1", "ParamsValue1")//
        //                .params("token", "3215sdf13ad1f65asd4f3ads1f");
    }

    /**
     * OkGo.<NetEntity>post("").tag(this)
     * .params(new ArrayMap<String></String>, String>())
     * .execute(new DialogCallback<NetEntity>(this) {}
    </NetEntity></NetEntity> */
    @Throws(Throwable::class)
    override fun convertResponse(response: Response): T? {
        if (type == null) {
            if (clazz == null) {
                val genType = javaClass.genericSuperclass
                type = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                val convert = JsonConvert(clazz)
                return convert.convertResponse(response)
            }
        }

        val convert = JsonConvert<T>(type)
        return convert.convertResponse(response)
    }
}