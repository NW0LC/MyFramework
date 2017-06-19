package cn.com.szw.demo;

import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static cn.com.szw.lib.myframework.app.MyApplication.salt;

public class MainActivity extends BaseActivity {

       @Override
    public boolean initToolbar() {
        return false;
    }

    @Override
    public int setInflateId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("MainTop", salt).toLowerCase());
        OkGo.<NetEntity<MainTopEntity>>post("http://www.fengwalker.com/App/BeeFrog/Main/MainTop.aspx")
                .params(params)
                .tag(this)
                .execute(new DialogCallback<NetEntity<MainTopEntity>>(this) {
            @Override
            public void onSuccess(Response<NetEntity<MainTopEntity>> response) {
                Toast.makeText(mContext, "response.body():" + response.body(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
