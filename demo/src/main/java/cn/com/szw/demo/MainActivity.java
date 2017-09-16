package cn.com.szw.demo;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends Activity {

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
    }
}
