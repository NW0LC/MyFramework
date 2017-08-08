package cn.com.szw.lib.myframework.utils.net.callback;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.net.UnknownHostException;
import java.util.Set;

import cn.com.szw.lib.myframework.utils.net.NetEntity;
import cn.com.szw.lib.myframework.view.CustomProgress;
import okhttp3.Call;
import okhttp3.Headers;


/**
 * ================================================
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T extends NetEntity> extends JsonCallback<T> {

    private Context context;



    public DialogCallback(Context context) {
        super();
        this.context =context;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        CustomProgress.show(context,"加载中",false,null);
    }

    @Override
    public void onFinish() {
        CustomProgress.disMiss();
    }

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        StringBuilder sb;
        Call call = response.getRawCall();
        if (call != null) {
            Throwable e = response.getException();
            if (e!=null) {
                if (e instanceof UnknownHostException) {
                    Toast.makeText(context, "网络未连接，请打开网络后再次尝试。", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Log.e("OkGo", "请求失败  请求方式：" + call.request().method() + "\n" + "url：" + call.request().url());
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
            Log.e("OkGo", sb.toString());
        }
    }
}