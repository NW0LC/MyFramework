package com.szw.framelibrary.utils.net

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader

import java.io.Reader
import java.lang.reflect.Type

object Convert {

    private fun create(): Gson = Convert.GsonHolder.gson

    private object GsonHolder {
         val gson = Gson()
    }

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(json: String, type: Class<T>): T = create().fromJson(json, type)

    fun <T> fromJson(json: String, type: Type): T = create().fromJson(json, type)

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(reader: JsonReader, typeOfT: Type): T = create().fromJson(reader, typeOfT)

    @Throws(JsonSyntaxException::class, JsonIOException::class)
    fun <T> fromJson(json: Reader, classOfT: Class<T>): T = create().fromJson(json, classOfT)

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun <T> fromJson(json: Reader, typeOfT: Type): T = create().fromJson(json, typeOfT)

    fun toJson(src: Any): String = create().toJson(src)

    fun toJson(src: Any, typeOfSrc: Type): String = create().toJson(src, typeOfSrc)
}