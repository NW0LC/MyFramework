package cn.com.szw.lib.myframework.utils.net.callback;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lzy.okgo.request.BaseRequest;

import java.net.UnknownHostException;

import cn.com.szw.lib.myframework.view.CustomProgress;


/**
 * ================================================
 * 描    述：对于网络请求是否需要弹出进度对话框
 * 修订历史：
 * ================================================
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private Context context;



    public DialogCallback(Context context) {
        super();
        this.context =context;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        CustomProgress.show(context,"加载中",false,null);
    }

    @Override
    public void onAfter(@Nullable T t, @Nullable Exception e) {
        super.onAfter(t, e);
        CustomProgress.disMiss();
        if (e!=null) {
            if (e instanceof UnknownHostException) {
                Toast.makeText(context, "网络未连接，请打开网络后再次尝试。", Toast.LENGTH_SHORT).show();
            }else
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}