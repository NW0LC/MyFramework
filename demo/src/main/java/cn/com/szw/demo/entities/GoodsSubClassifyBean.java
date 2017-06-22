package cn.com.szw.demo.entities;

import io.realm.RealmObject;

public class GoodsSubClassifyBean extends RealmObject implements GoodsEntity.OnClassifyChange {
    /**
     * goodsSubClassifyName : 黑色
     * goodsSubClassifyId : 1
     * goodsSubClassifyUrl :
     * goodsSubClassifyNextId : 1,2,3,4
     * goodsSubState : 1 on  2 off 3 pass
     */

    private String goodsSubClassifyName;
    private String goodsSubClassifyId;
    private String goodsSubClassifyUrl;
    private String goodsSubClassifyNextId;
    private String goodsSubState = "2";


    public String getGoodsSubState() {
        return goodsSubState;
    }

    public void setGoodsSubState(String goodsSubState) {
        this.goodsSubState = goodsSubState;
    }

    public String getGoodsSubClassifyName() {
        return goodsSubClassifyName;
    }

    public void setGoodsSubClassifyName(String goodsSubClassifyName) {
        this.goodsSubClassifyName = goodsSubClassifyName;
    }

    public String getGoodsSubClassifyId() {
        return goodsSubClassifyId;
    }

    public void setGoodsSubClassifyId(String goodsSubClassifyId) {
        this.goodsSubClassifyId = goodsSubClassifyId;
    }

    public String getGoodsSubClassifyUrl() {
        return goodsSubClassifyUrl;
    }

    public void setGoodsSubClassifyUrl(String goodsSubClassifyUrl) {
        this.goodsSubClassifyUrl = goodsSubClassifyUrl;
    }

    public String getGoodsSubClassifyNextId() {
        return goodsSubClassifyNextId;
    }

    public void setGoodsSubClassifyNextId(String goodsSubClassifyNextId) {
        this.goodsSubClassifyNextId = goodsSubClassifyNextId;
    }

    @Override
    public void onChange(String tag) {
        boolean isMatch=false;
        for (String s : tag.split(",")) {
            if (s.equals(getGoodsSubClassifyId())) {
                isMatch=true;
                break;
            }
        }
        if (isMatch) {
            if (getGoodsSubState().equals("3"))
                setGoodsSubState("2");
        }else{
            setGoodsSubState("3");
        }
    }
}