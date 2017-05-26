package cn.com.szw.lib.myframework.utils.net;


/**
 * Created by Administrator on 2016/7/5.
 */
//@HttpResponse(parser = JsonResponseParser.class)//每一个实体类必须加这句话，别忘了在baseactivity里面 初始化注解 x.view().inject(this);
public class NetEntity<T> {
//    "code":"200",
//            "message":"提示"，
//            "data": "1234"

    private String code;
    private String message;
    private T data;
    private T info;

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public int getCode() {
        return Integer.valueOf(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
