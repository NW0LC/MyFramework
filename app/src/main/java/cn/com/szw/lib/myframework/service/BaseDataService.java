package cn.com.szw.lib.myframework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.com.szw.lib.myframework.utils.RxBus;

/**
 * Created by 史忠文
 * on 2017/2/28.
 */

public class BaseDataService extends Service {
    public static final String Data_city = "Data_city";
    public static final String Data_class = "Data_Class";
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            RxBus.get().register(this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        initCityData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            RxBus.get().unregister(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

//    private void initCityData() {
//        Map<String, String> map = new HashMap<>();
//        map.put("requestCheck", EncryptUtils.encryptMD5ToString("OpenedCityList", salt).toLowerCase());
//        OkGo.post(Urls.url).tag(this)
//                .params(map)
//                .execute(new JsonCallback<NetEntity<List<FilterPopEntity>>>() {
//
//                    @Override
//                    public void onSuccess(NetEntity<List<FilterPopEntity>> responseData, Call call, Response response) {
//                        if (SUCCESS==responseData.getCode()) {
////                            responseData.getData().add(0,new FilterPopEntity("0","全部"));
//                            spUtils.put(Data_city, JSON.toJSONString(responseData.getData()));
//                            RxBus.get().post(Receiver_BaseLocation,Receiver_BaseLocation);
//                        }
//                    }
//
//                });
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
