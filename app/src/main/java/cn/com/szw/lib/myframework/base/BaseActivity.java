package cn.com.szw.lib.myframework.base;

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

import com.lzy.imagepicker.view.SystemBarTintManager;

import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.view.CustomProgress;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static cn.com.szw.lib.myframework.config.Constants.Permission.Camera;
import static cn.com.szw.lib.myframework.config.Constants.Permission.Location;
import static cn.com.szw.lib.myframework.config.Constants.Permission.Phone;


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
        ButterKnife.bind(this);
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
    public void showCameraWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.showCameraWithCheck(this, intent,requestCode,isService);
    }

    @Override
    public void showCameraWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.showCameraWithCheck(this, intent,-1,isService);
    }

    @Override
    public void locationAndSMSWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.locationAndSMSWithCheck(this, intent,requestCode,isService);
    }

    @Override
    public void locationAndSMSWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.locationAndSMSWithCheck(this, intent,-1,isService);
    }

    @Override
    public void callPhoneWithCheck(Intent intent, int requestCode, boolean isService) {
        BaseActivityPermissionsDispatcher.callPhoneWithCheck(this, intent,requestCode,isService);
    }

    @Override
    public void callPhoneWithCheck(Intent intent, boolean isService) {
        BaseActivityPermissionsDispatcher.callPhoneWithCheck(this, intent,-1,isService);
    }

    @NeedsPermission({CAMERA, WRITE_EXTERNAL_STORAGE})
    void showCamera(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Camera:requestCode);
    }

    @NeedsPermission({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, RECEIVE_SMS, READ_SMS})
    void locationAndSMS(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Location:requestCode);
    }

    @NeedsPermission(CALL_PHONE)
    void callPhone(Intent intent,int requestCode, boolean isService) {
        startAction(intent, isService,requestCode==-1?Phone:requestCode);
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

    @Override
    protected void onDestroy() {
        try {
            RxBus.get().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomProgress.getsPrograss().dismiss();
        super.onDestroy();
    }


}
