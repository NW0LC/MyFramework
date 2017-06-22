package cn.com.szw.demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import cn.com.szw.demo.R;

/**
 * Created by 史忠文
 * on 2017/6/22.
 */

public class MainAdapter<T extends String> extends BaseQuickAdapter<T,BaseViewHolder> {
    public MainAdapter() {
        super(R.layout.item_main, new ArrayList<T>());
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {

    }

}
