package com.szw.framelibrary.config

/**
 * 存在手机内的信息
 * @author swz
 */

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

object PreferencesService {
    private val DATA_CONTEXT = "dataContext"
    private val DOWN_TIMER = "down_timer"

    /**
     * @param accountKey 账号
     * @param accountValue 密码
     */
    fun saveAccount(context: Context, accountKey: String, accountValue: String) {

        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("accountKey", accountKey)
        editor.putString("accountValue", accountValue)
        editor.apply()
    }

    /**
     * @param context  上下文
     * @param autoLoginToken 自动登录秘钥
     */
    fun saveAutoLoginToken(context: Context, autoLoginToken: String) {

        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("autoLoginToken", autoLoginToken)
        editor.apply()
    }

    /**
     * @return  获取自动登录秘钥
     */
    fun getAutoLoginToken(context: Context): String? {
        val accountKey: String?
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        accountKey = preferences.getString("autoLoginToken", null)
        return accountKey
    }

    /**
     * @return  获取账户名
     */
    fun getAccountKey(context: Context): String? {
        val accountKey: String?
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        accountKey = preferences.getString("accountKey", null)
        return accountKey
    }

    /**
     * @return  获取密码
     */
    fun getAccountValue(context: Context): String? {
        val accountValue: String?
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        accountValue = preferences.getString("accountValue", null)
        return accountValue
    }


    /**
     * @param city 城市
     */
    fun saveCity(context: Context, city: String) {
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("city", city)
        editor.apply()
    }

    /**
     * @return 获取城市
     */
    fun getCity(context: Context): String? {
        val pass: String?
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        pass = preferences.getString("city", null)
        return pass
    }

    /** 保存数据
     * @param key  键
     * @param value 值
     */
    fun saveData(context: Context, key: String, value: String) {
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**获取数据
     * @param key 键
     * @return 值
     */
    fun getData(context: Context, key: String): String? {
        val String: String?
        val preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE)
        String = preferences.getString(key, null)
        return String
    }


    /**
     * @param context 上下文
     * @param time  当前时间
     */
    fun setDownTimer(context: Context, key: String, time: Long) {
        //实例化SharedPreferences对象（第一步）
        val mySharedPreferences = context.getSharedPreferences(DOWN_TIMER,
                Context.MODE_PRIVATE)
        //实例化SharedPreferences.Editor对象（第二步）
        val editor = mySharedPreferences.edit()
        //用putString的方法保存数据
        editor.putLong(key, time)
        //提交当前数据
        editor.apply()
    }

    /**
     * @param context  上下文
     * @return 保存时间
     */
    fun getDownTimer(context: Context, key: String): Long {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        val sharedPreferences = context.getSharedPreferences(DOWN_TIMER,
                Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, 0)
    }
}
