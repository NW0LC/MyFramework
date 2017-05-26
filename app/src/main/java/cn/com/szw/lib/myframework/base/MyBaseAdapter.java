package cn.com.szw.lib.myframework.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swain
 * on 2016/9/23.
 */

public abstract  class MyBaseAdapter<T> extends BaseAdapter {
    private List<T> objects = new ArrayList<>();

    protected Context context;
    protected LayoutInflater layoutInflater;

    /**
     * 返回适配器中的数据
     */
    public List<T> getAdapterData() {
        return objects;
    }

    /**
     * 封装添加一条记录方法
     * t 一条数据的对象
     * isClearOld 是否清除原数据
     */
    public void appendData(T t, boolean isClearOld) {
        if (t == null) {//非空验证
            return;
        }
        if (isClearOld) {//如果true 清空原数据
            objects.clear();
        }//添加一条新数据到最后
        objects.add(t);

    }

    /**
     * 添加多条记录
     *
     * @param aList      数据集合
     * @param isClearOld 是否清空原数据
     */
    public void addendData(List<T> aList, boolean isClearOld) {
        if (aList == null) {
            return;
        }
        if (isClearOld) {
            objects.clear();
        }
        objects.addAll(aList);

    }

    /**
     * 添加一条记录 到第一条
     *
     * @param t data
     * @param isClearOld clear
     */
    public void appendDataTop(T t, boolean isClearOld) {
        if (t == null) { //非空验证
            return;
        }
        if (isClearOld) {//如果true 清空原数据
            objects.clear();
        }//添加一条新数据到第一条
        objects.add(0, t);

    }

    /**
     * 添加多条记录到顶部
     *
     * @param aList      数据集合
     * @param isClearOld 是否清空原数据
     */
    public void addendDataTop(ArrayList<T> aList, boolean isClearOld) {
        if (aList == null) {
            return;
        }
        if (isClearOld) {
            objects.clear();
        }
        objects.addAll(0, aList);

    }

    /**
     * 更新适配器数据
     */
    public void updateAdapter() {
        this.notifyDataSetChanged();
    }

    /**
     * 清空适配器数据
     */
    public void clearAdapter() {
        objects.clear();
    }

    public MyBaseAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
