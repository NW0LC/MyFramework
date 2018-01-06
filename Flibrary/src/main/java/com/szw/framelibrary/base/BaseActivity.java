package com.szw.framelibrary.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.hwangjr.rxbus.RxBus;
import com.lzy.imagepicker.view.SystemBarTintManager;
import com.szw.framelibrary.R;
import com.szw.framelibrary.config.Constants;
import com.szw.framelibrary.view.CustomProgress;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static anet.channel.util.Utils.context;


/**
 * Created by Swain
 * on 2017/1/16.
 */
@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity implements AbsBaseActivity {
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(setInflateId());
        mContext = this;
        PushAgent.getInstance(context).onAppStart();
        try {
            init();
            init(savedInstanceState);
            initBar();
            RxBus.get().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initBar() {
        if (initToolbar()) {
            // 4.4及以上版本开启
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);

            // 自定义颜色
            tintManager.setTintColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 视图，组件,数据的初始化
     */
    @Override
    public void init() throws Exception {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void PermissionCameraWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionCameraWithPermissionCheck(this, intent,requestCode,isService);
    }

    @Override
    public void PermissionCameraWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionCameraWithPermissionCheck(this, intent,-1,isService);
    }

    @Override
    public void PermissionLocationWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionLocationWithPermissionCheck(this, intent,requestCode,isService);
    }

    @Override
    public void PermissionLocationWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionLocationWithPermissionCheck(this, intent,-1,isService);
    }
    @Override
    public void PermissionSMSWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionSMSWithPermissionCheck(this, intent,requestCode,isService);
    }

    @Override
    public void PermissionSMSWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionSMSWithPermissionCheck(this, intent,-1,isService);
    }

    @Override
    public void PermissionCallPhoneWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionCallPhoneWithPermissionCheck(this, intent,requestCode,isService);
    }

    @Override
    public void PermissionCallPhoneWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.PermissionCallPhoneWithPermissionCheck(this, intent,-1,isService);
    }

    @NeedsPermission({CAMERA, WRITE_EXTERNAL_STORAGE})
    void PermissionCamera(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1? Constants.Permission.INSTANCE.getCamera():requestCode);
    }

    @NeedsPermission({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    void PermissionLocation(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Constants.Permission.INSTANCE.getLocation():requestCode);
    }

    @NeedsPermission({RECEIVE_SMS, READ_SMS})
    void PermissionSMS(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Constants.Permission.INSTANCE.getSMS():requestCode);
    }

    @NeedsPermission(CALL_PHONE)
    void PermissionCallPhone(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Constants.Permission.INSTANCE.getPhone():requestCode);
    }

    void startAction(Intent intent, boolean isService,int requestCode) {
        if (intent != null) {
            if (isService)
                startService(intent);
            else
                startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    public void onResume() {
        super.onResume();
//      友盟统计
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
//      友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        try {
            RxBus.get().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomProgress.Companion.disMiss();
        super.onDestroy();
    }


}
