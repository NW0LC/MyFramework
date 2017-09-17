package cn.com.szw.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.szw.framelibrary.utils.net.NetEntity;
import com.szw.framelibrary.utils.net.callback.DialogCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements OnRefreshListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this);
        RecyclerView rec = findViewById(R.id.recyclerView);
        StatusBarUtil.setPaddingSmart(this, rec);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.toolbar));
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurview));
        StatusBarUtil.setMargin(this, findViewById(R.id.gifview));

        rec.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter adapter = new AddressAdapter();
        rec.setAdapter(adapter);
        ArrayList<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        adapter.setNewData(data);
        HashMap<String, String> params = new HashMap<>();
        OkGo.<NetEntity<String>>post("http://www.fengwalker.com/App/BeeFrog/Main/MainTop.aspx")
                .params(params)
                .tag(this)
                .execute(new DialogCallback<NetEntity<String>>(this) {
                    @Override
                    public void onSuccess(Response<NetEntity<String>> response) {
                        Toast.makeText(MainActivity.this, "response.body():" + response.body(), Toast.LENGTH_SHORT).show();
                    }
                });

        SmartRefreshLayout viewById = findViewById(R.id.refreshLayout);
        viewById.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
    startActivity(new Intent(this,OtherActivity.class));
    }
}
