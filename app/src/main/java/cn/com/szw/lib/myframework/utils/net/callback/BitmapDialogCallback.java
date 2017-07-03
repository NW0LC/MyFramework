package cn.com.szw.lib.myframework.utils.net.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Set;

import cn.com.szw.lib.myframework.view.CustomProgress;
import okhttp3.Call;
import okhttp3.Headers;

/**
 * ================================================
 * 描    述：请求图图片的时候显示对话框
 * ================================================
 */
public abstract class BitmapDialogCallback extends BitmapCallback {
    private Context context;



    public BitmapDialogCallback(Context context) {
        super();
        this.context =context;
    }
    @Override
    public void onStart(Request<Bitmap, ? extends Request> request) {
        super.onStart(request);
        CustomProgress.show(context,"加载中",false,null);
    }

    @Override
    public void onFinish() {
        CustomProgress.disMiss();
    }

    @Override
    public void onError(Response<Bitmap> response) {
        super.onError(response);
        StringBuilder sb;
        Call call = response.getRawCall();
        if (call != null) {
            Toast.makeText(context, "请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url(), Toast.LENGTH_SHORT).show();
        }
        okhttp3.Response rawResponse = response.getRawResponse();
        if (rawResponse != null) {
            Headers responseHeadersString = rawResponse.headers();
            Set<String> names = responseHeadersString.names();
            sb = new StringBuilder();
            sb.append("stateCode ： ").append(rawResponse.code()).append("\n");
            for (String name : names) {
                sb.append(name).append(" ： ").append(responseHeadersString.get(name)).append("\n");
            }
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}