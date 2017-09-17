package com.szw.framelibrary.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

object CloneUtils {
    fun <T : Serializable> clone(obj: T): T? {
        var cloneObj: T? = null
        try {
            //写入字节流
            val out = ByteArrayOutputStream()
            val obs = ObjectOutputStream(out)
            obs.writeObject(obj)
            obs.close()

            //分配内存，写入原始对象，生成新对象
            val ios = ByteArrayInputStream(out.toByteArray())
            val ois = ObjectInputStream(ios)
            //返回生成的新对象
            cloneObj = ois.readObject() as T
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cloneObj
    }
}  