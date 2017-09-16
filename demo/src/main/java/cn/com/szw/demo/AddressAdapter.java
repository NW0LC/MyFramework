package cn.com.szw.demo;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

public class AddressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public AddressAdapter() {
        super(R.layout.listitem_practive_repast, new ArrayList<String>());
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }


}
