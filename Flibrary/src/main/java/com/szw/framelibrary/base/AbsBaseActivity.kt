package com.szw.framelibrary.base

import android.content.Intent
import android.os.Bundle

/**
 * Created by 史忠文
 * on 2017/3/20.
 */

internal interface AbsBaseActivity {
    /**
     * 视图，组件,数据的初始化
     */
    @Throws(Exception::class)
    fun init()

    fun init(savedInstanceState: Bundle?)
    /**
     * 初始化电量条和状态栏
     */
    fun initBar()

    fun initToolbar(): Boolean
    /**
     * 设置布局id
     */
    fun setInflateId(): Int

    /**
     * 设置权限
     * BaseActivityPermissionsDispatcher.showCameraWithCheck(this);
     * BaseActivityPermissionsDispatcher.locationAndSMSWithCheck(this);
     * BaseActivityPermissionsDispatcher.callPhoneWithCheck(this);
     */
    fun PermissionCameraWithCheck(intent: Intent?, isService: Boolean) //拍照与读取内存卡，并启动意图，null 不启动

    fun PermissionLocationWithCheck(intent: Intent?, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun PermissionSMSWithCheck(intent: Intent?, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun PermissionCallPhoneWithCheck(intent: Intent?, isService: Boolean) //电话，并启动意图，null 不启动
    fun PermissionCameraWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //拍照与读取内存卡，并启动意图，null 不启动
    fun PermissionLocationWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun PermissionSMSWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //定位及读取短信。并启动意图，null 不启动
    fun PermissionCallPhoneWithCheck(intent: Intent?, requestCode: Int, isService: Boolean) //电话，并启动意图，null 不启动
}
