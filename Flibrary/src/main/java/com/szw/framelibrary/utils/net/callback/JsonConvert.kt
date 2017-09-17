package com.szw.framelibrary.utils.net.callback

import com.google.gson.stream.JsonReader
import com.lzy.okgo.convert.Converter
import com.szw.framelibrary.utils.net.Convert
import com.szw.framelibrary.utils.net.NetEntity
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class JsonConvert<T:NetEntity<*>> : Converter<T> {

    private var type: Type? = null
    private var clazz: Class<T>?=null


    constructor(type: Type?) {
        this.type = type
    }

    constructor(clazz: Class<T>?) {
        this.clazz = clazz
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Throws(Throwable::class)
    override fun convertResponse(response: Response): T? {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                val genType = javaClass.genericSuperclass
                type = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                return parseClass(response, clazz)
            }
        }

        return if (type is ParameterizedType) {
            parseParameterizedType(response, type as ParameterizedType?)
        } else if (type is Class<*>) {
            parseClass(response, type as Class<*>?)
        } else {
            parseType(response, type)
        }
    }

    @Throws(Exception::class)
    private fun parseClass(response: Response, rawType: Class<*>?): T? {
        if (rawType == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())

        if (rawType == String::class.java) {

            return body.string() as T
        } else if (rawType == JSONObject::class.java) {

            return JSONObject(body.string()) as T
        } else if (rawType == JSONArray::class.java) {

            return JSONArray(body.string()) as T
        } else {
            val t = Convert.fromJson<T>(jsonReader, rawType)
            response.close()
            return t
        }
    }

    @Throws(Exception::class)
    private fun parseType(response: Response, type: Type?): T? {
        if (type == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        val t = Convert.fromJson<T>(jsonReader, type)
        response.close()
        return t
    }

    @Throws(Exception::class)
    private fun parseParameterizedType(response: Response, type: ParameterizedType?): T? {
        if (type == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())
        //        String jsonReader =body.string();

        val rawType = type.rawType                     // 泛型的实际类型
        val typeArgument = type.actualTypeArguments[0] // 泛型的参数
        if (rawType !== NetEntity::class.java) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            val t = Convert.fromJson<T>(jsonReader, type)
            response.close()
            return t
        } else {
            if (typeArgument === Void::class.java) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                //                SimpleResponse simpleResponse = JSON.parseObject(jsonReader, SimpleResponse.class);
                val simpleResponse = Convert.fromJson<T>(jsonReader, NetEntity::class.java)
                response.close()

                return simpleResponse
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                //                NetEntity netEntity =JSON.parseObject(jsonReader, type);
                val netEntity = Convert.fromJson<T>(jsonReader, type)
                response.close()
                val code = netEntity.getCode()
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                //                if (code == 200) {

                return netEntity
                //                } else if (code == 104) {
                //                    throw new IllegalStateException("用户授权信息无效");
                //                } else if (code == 105) {
                //                    throw new IllegalStateException("用户收取信息已过期");
                //                } else {
                //                    //直接将服务端的错误信息抛出，onError中可以获取
                //                    throw new IllegalStateException(netEntity.getMessage());
                //                }
            }
        }
    }
}