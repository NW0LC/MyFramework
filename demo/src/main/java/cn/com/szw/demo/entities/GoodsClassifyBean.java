package cn.com.szw.demo.entities;

import io.realm.RealmList;
import io.realm.RealmObject;

public class GoodsClassifyBean extends RealmObject {
    /**
     * goodsClassifyName : 颜色
     * goodsClassifyRealmList : [{"goodsSubClassifyName":"黑色","goodsSubClassifyId":"1","goodsSubClassifyUrl":"","goodsSubClassifyNextId":"1,2,3,4"}]
     */

    private String goodsClassifyName;
    private RealmList<GoodsSubClassifyBean> goodsSubClassify;
    private boolean isExtra;

    public boolean isExtra() {
        return isExtra;
    }

    public void setExtra(boolean extra) {
        isExtra = extra;
    }

    public String getGoodsClassifyName() {
        return goodsClassifyName;
    }

    public void setGoodsClassifyName(String goodsClassifyName) {
        this.goodsClassifyName = goodsClassifyName;
    }

    public RealmList<GoodsSubClassifyBean> getGoodsSubClassify() {
        return goodsSubClassify;
    }

    public void setGoodsSubClassify(RealmList<GoodsSubClassifyBean> goodsSubClassify) {
        this.goodsSubClassify = goodsSubClassify;
    }
}