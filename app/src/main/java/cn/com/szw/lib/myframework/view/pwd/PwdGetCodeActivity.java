package cn.com.szw.lib.myframework.view.pwd;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.lzy.okgo.OkGo;
import com.yanzhenjie.permission.AndPermission;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.app.MyApplication;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.PreferencesService;
import cn.com.szw.lib.myframework.config.Urls;
import cn.com.szw.lib.myframework.observer.SmsContentObserver;
import cn.com.szw.lib.myframework.utils.SZWUtils;
import cn.com.szw.lib.myframework.utils.StringUtil;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.ClearWriteEditText;
import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static cn.com.szw.lib.myframework.app.MyApplication.salt;
import static cn.com.szw.lib.myframework.config.Constants.Permission.Location;

public class PwdGetCodeActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.mRightImg)
    ImageView mRightImg;
    @BindView(R.id.mLeftImg)
    ImageView mLeftImg;
    @BindView(R.id.parent_lay)
    RelativeLayout parentLay;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_phone)
    ClearWriteEditText phone;
    @BindView(R.id.get_code)
    TextView getSecurityCode;
    @BindView(R.id.ed_code)
    ClearWriteEditText code;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.bt_next)
    Button btNext;
    private CountDownTimer countDownTimer;
    private int time = 120000;//倒计时时间
    private String downKey = "R";
    private SmsContentObserver smsContentObserver;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("验证手机");
        return false;
    }

    @Override
    public void init() {
        long l = System.currentTimeMillis();
        if (PreferencesService.getDownTimer(this, downKey) > 0 && l > PreferencesService.getDownTimer(this, downKey)) {
            downTimer(time - (l - PreferencesService.getDownTimer(this, downKey)));
        }
        // 先判断是否有权限。
        if (!AndPermission.hasPermission(this, RECEIVE_SMS, READ_SMS)) {
            // 申请权限。
            Log.i("Tag", "申请权限.");
            AndPermission.with(this)
                    .requestCode(Location)
                    .callback(this)
                    .permission(RECEIVE_SMS, READ_SMS)
                    .start();
        }
        // 注册读取短信Observer
        smsContentObserver = SZWUtils.registerSMS(mContext, SZWUtils.patternCode(mContext, getSecurityCode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册读取短信Observer
        getContentResolver().unregisterContentObserver(smsContentObserver);

    }

    private void downTimer(long l) {
        countDownTimer = new CountDownTimer(l, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resetTimer(false, millisUntilFinished);
            }

            @Override
            public void onFinish() {
                resetTimer(true, Long.MIN_VALUE);
            }
        };
        countDownTimer.start();
    }

    private void resetTimer(boolean b, long millisUntilFinished) {
        if (b) {
            countDownTimer.cancel();
            getSecurityCode.setText("点击获取");
            getSecurityCode.setClickable(true);
            getSecurityCode.setTextColor(ContextCompat.getColor(this, R.color.green_6cc236));
            getSecurityCode.setBackgroundResource(R.drawable.login_bg_line);
            getSecurityCode.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));
            PreferencesService.setDownTimer(this, downKey, 0);
        } else {
            getSecurityCode.setClickable(false);
            getSecurityCode.setBackground(ContextCompat.getDrawable(this, R.drawable.login_grey_lin));
            getSecurityCode.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));
            getSecurityCode.setTextColor(ContextCompat.getColor(this, R.color.MaterialGrey400));
            getSecurityCode.setText("重发(" + millisUntilFinished / 1000 + ")s");
        }

    }

    @Override
    public int setInflateId() {
        return R.layout.pwd_activity_get_code;
    }


    @OnClick({R.id.mLeftImg, R.id.get_code, R.id.bt_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mLeftImg:
                finish();
                break;
            case R.id.get_code:
                getSecurityCode();
                break;
            case R.id.bt_next:
                check();
                break;
        }
    }

    private void check() {
        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            phone.setShakeAnimation();
        } else if (!StringUtil.isPhone(phone.getText().toString())) {
            phone.setShakeAnimation();
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code.getText().toString().trim())) {
            code.setShakeAnimation();
        }  else {
            checkCode();

        }
    }
    /**
     *  验证验证码
     */
    private void checkCode() {
//        userId	string	必填	会员id
//        buyCardRecordId	string	必填	会员购买卡种记录id
//        requestCheck	string	必填	验证请求
        Map<String, String> map = new HashMap<>();
        map.put("userId", MyApplication.getLoginUserId());
        map.put("code", code.getText().toString().trim());
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId(), salt).toLowerCase());
        OkGo.post(Urls.CheckCode).tag(this)
                .params(map)
                .execute(new DialogCallback<NetEntity<Void>>(this) {

                    @Override
                    public void onSuccess(NetEntity<Void> responseData, Call call, Response response) {
                        Intent intent = new Intent(mContext,PwdSetActivity.class);
                        startActivityForResult(intent,100);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Intent intent = new Intent(mContext,PwdSetActivity.class);
                        startActivityForResult(intent,100);
                    }
                });

    }
    private void getSecurityCode() {
        if (TextUtils.isEmpty(phone.getText().toString().trim()) || !StringUtil.isPhone(phone.getText().toString())) {
            phone.setShakeAnimation();
        } else {
            downTimer(time);
            PreferencesService.setDownTimer(this, downKey, System.currentTimeMillis());
            Map<String, String> map = new HashMap<>();
            final String mobile = phone.getText().toString().trim();
            map.put("phone", TextUtils.isEmpty(mobile) ? "" : mobile);
            map.put("purpose", "3");
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile, salt).toLowerCase());
            OkGo.post(Urls.ObtainCode).tag(this)
                    .params(map)
                    .execute(new DialogCallback<NetEntity<String>>(this) {

                        @Override
                        public void onSuccess(NetEntity<String> responseData, Call call, Response response) {
                            Toast.makeText(mContext, responseData.getMessage(), Toast.LENGTH_SHORT).show();
                            code.setText(responseData.getData());
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            resetTimer(true, Long.MIN_VALUE);
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            finish();
        }
    }
}