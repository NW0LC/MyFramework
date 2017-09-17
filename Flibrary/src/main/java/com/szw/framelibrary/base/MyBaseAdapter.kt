package com.szw.framelibrary.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter
import java.util.*

/**
 * Created by Swain
 * on 2016/9/23.
 */

abstract class MyBaseAdapter<T>(protected var context: Context) : BaseAdapter() {
    private val objects = ArrayList<T>()
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    /**
     * 返回适配器中的数据
     */
    val adapterData: List<T>
        get() = objects

    /**
     * 封装添加一条记录方法
     * t 一条数据的对象
     * isClearOld 是否清除原数据
     */
    fun appendData(t: T?, isClearOld: Boolean) {
        if (t == null) {//非空验证
            return
        }
        if (isClearOld) {//如果true 清空原数据
            objects.clear()
        }//添加一条新数据到最后
        objects.add(t)

    }

    /**
     * 添加多条记录
     *
     * @param aList      数据集合
     * @param isClearOld 是否清空原数据
     */
    fun addendData(aList: List<T>?, isClearOld: Boolean) {
        if (aList == null) {
            return
        }
        if (isClearOld) {
            objects.clear()
        }
        objects.addAll(aList)

    }

    /**
     * 添加一条记录 到第一条
     *
     * @param t data
     * @param isClearOld clear
     */
    fun appendDataTop(t: T?, isClearOld: Boolean) {
        if (t == null) { //非空验证
            return
        }
        if (isClearOld) {//如果true 清空原数据
            objects.clear()
        }//添加一条新数据到第一条
        objects.add(0, t)

    }

    /**
     * 添加多条记录到顶部
     *
     * @param aList      数据集合
     * @param isClearOld 是否清空原数据
     */
    fun addendDataTop(aList: ArrayList<T>?, isClearOld: Boolean) {
        if (aList == null) {
            return
        }
        if (isClearOld) {
            objects.clear()
        }
        objects.addAll(0, aList)

    }

    /**
     * 更新适配器数据
     */
    fun updateAdapter() {
        this.notifyDataSetChanged()
    }

    /**
     * 清空适配器数据
     */
    fun clearAdapter() {
        objects.clear()
    }

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): T {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
