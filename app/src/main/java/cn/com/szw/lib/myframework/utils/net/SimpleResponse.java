package cn.com.szw.lib.myframework.utils.net;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public String code;
    public String msg;

    public NetEntity toNetEntity() {
        NetEntity netEntity = new NetEntity();
        netEntity.setCode(code);
        netEntity.setMessage(msg);
        return netEntity;
    }
}