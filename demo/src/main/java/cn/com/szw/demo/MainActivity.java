package cn.com.szw.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import cn.com.szw.demo.adapter.MainAdapter;
import cn.com.szw.demo.entities.MainTopEntity;
import cn.com.szw.lib.myframework.base.BaseActivity;
import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.utils.net.callback.DialogCallback;

import static cn.com.szw.lib.myframework.app.MyApplication.salt;

public class MainActivity extends BaseActivity {
    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    @BindView(R.id.recycler)
    RecyclerView recycler;

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
    recycler.setLayoutManager(new LinearLayoutManager(mContext));
        MainAdapter<String> mainAdapter = new MainAdapter<>();
        mainAdapter.bindToRecyclerView(recycler);
        ArrayList<String> data = new ArrayList<>();
        data.add("");

        mainAdapter.setNewData(data);

//        RealmHelper<GoodsEntity> realmHelper = new RealmHelper<GoodsEntity>(){};
//        List<GoodsEntity> goodsEntities = realmHelper.queryAllEntities(primaryKey);

      recycler.addOnItemTouchListener(new OnItemClickListener() {
          @Override
          public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
              getPermissions();
          }
      });
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("hello-world.json");
        animationView.loop(true);
        animationView.playAnimation();
    }


    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            Log.i("CJT", "picture");
            String path = data.getStringExtra("path");
//            photo.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        if (resultCode == 102) {
            Log.i("CJT", "video");
            String path = data.getStringExtra("path");
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), 100);
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
