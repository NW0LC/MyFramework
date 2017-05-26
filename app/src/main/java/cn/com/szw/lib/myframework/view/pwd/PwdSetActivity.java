package cn.com.szw.lib.myframework.view.pwd;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.lzy.okgo.OkGo;

import java.util.HashMap;
import java.util.Map;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.app.MyApplication;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.config.Urls;
import cn.com.szw.lib.myframework.utils.DialogUtils;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;
import cn.com.szw.lib.myframework.view.pwd.widget.OnPasswordInputFinish;
import cn.com.szw.lib.myframework.view.pwd.widget.PasswordView;
import okhttp3.Call;
import okhttp3.Response;

import static cn.com.szw.lib.myframework.app.MyApplication.salt;

/**
 * Created by 史忠文
 * on 2017/5/4.
 */

public class PwdSetActivity extends BaseActivity implements OnPasswordInputFinish {
    TextView mTitle;
    ImageView mRightImg;
    Toolbar toolbar;
    PasswordView mPasswordView;

    @Override
    public boolean initToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);
        mTitle.setTextSize(18);
        mTitle.setText("设置支付密码");
        mRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.pwd_activity_set;
    }

    @Override
    public void init() {
        mRightImg = (ImageView) findViewById(R.id.mRightImg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.mTitle);
        mPasswordView = (PasswordView) findViewById(R.id.mPasswordView);
        mPasswordView.getVirtualKeyboardView().getLayoutBack().setVisibility(View.GONE);
        mPasswordView.setOnFinishInput(this);
    }

    @Override
    public void inputFinish(String password) {
        setPayPwd(password);
    }
    /**
     *  设置支付密码
     */
    private void setPayPwd(String password) {
//        userId	string	必填	会员id
//        buyCardRecordId	string	必填	会员购买卡种记录id
//        requestCheck	string	必填	验证请求
        Map<String, String> map = new HashMap<>();
        map.put("userId", MyApplication.getLoginUserId());
        map.put("payPwd", password);
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.getLoginUserId(), salt).toLowerCase());
        OkGo.post(Urls.SetPayPwd).tag(this)
                .params(map)
                .execute(new DialogCallback<NetEntity<Void>>(this) {

                    @Override
                    public void onSuccess(NetEntity<Void> responseData, Call call, Response response) {
                        DialogUtils.WarningForListener(mContext, responseData.getMessage(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        DialogUtils.WarningForListener(mContext, e.getMessage(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                });

    }
}
