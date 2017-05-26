package cn.com.szw.lib.myframework.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.imagepicker.view.SystemBarTintManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.ButterKnife;
import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.utils.RxBus;
import cn.com.szw.lib.myframework.view.CustomProgress;

import static cn.com.szw.lib.myframework.config.Constants.Permission.Location;


/**
 * Created by Swain
 * on 2017/1/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements AbsBaseActivity, PermissionListener {
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            tintManager.setTintColor(ContextCompat.getColor(mContext, R.color.app_bg));
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
    public void onSucceed(int requestCode, @NonNull List<String> grantedPermissions) {
        // 权限申请成功回调。
        Log.i("Tag", "权限申请成功回调。requestCode=" + requestCode);
        if (requestCode == Location) {
            Intent startLocationServiceIntent = new Intent(mContext,
                    LocationService.class);
            startService(startLocationServiceIntent);
        }
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        // 权限申请失败回调。
        Log.i("Tag", "权限申请失败回调。requestCode=" + requestCode);
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(BaseActivity.this, deniedPermissions)) {
            // 第一种：用默认的提示语。
//                AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

//                 第二种：用自定义的提示语。
            AndPermission.defaultSettingDialog(BaseActivity.this, requestCode)
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();

            // 第三种：自定义dialog样式。
            // SettingService settingService =
            //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
            // 你的dialog点击了确定调用：
            // settingService.execute();
            // 你的dialog点击了取消调用：
            // settingService.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            RxBus.get().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomProgress.disMiss();
        super.onDestroy();
    }


}
