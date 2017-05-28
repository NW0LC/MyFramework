package cn.com.szw.lib.myframework.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 史忠文
 * on 2017/3/20.
 */

interface AbsBaseActivity {
    /**
     * 视图，组件,数据的初始化
     */
    void init() throws Exception;
    void init(Bundle savedInstanceState);
    /**
     * 初始化电量条和状态栏
     */
    void initBar();
    boolean initToolbar();
    /**
     * 设置布局id
     */
    int setInflateId();
    /**
     * 设置权限
     * BaseActivityPermissionsDispatcher.showCameraWithCheck(this);
     * BaseActivityPermissionsDispatcher.locationAndSMSWithCheck(this);
     * BaseActivityPermissionsDispatcher.callPhoneWithCheck(this);
     */
    void showCameraWithCheck(Intent intent);//拍照与读取内存卡，并启动意图，null 不启动
    void locationAndSMSWithCheck(Intent intent);//定位及读取短信。并启动意图，null 不启动
    void callPhoneWithCheck(Intent intent);//电话，并启动意图，null 不启动
}
