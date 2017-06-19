package cn.com.szw.demo;

import io.realm.RealmObject;

public class GoodsClassifyPoolBean extends RealmObject {
    /**
     * poolId :
     * poolKey :
     * goodsClassifyInventory : 分类库存
     * goodsClassifyPrice : 分类价格
     */

    private String poolId;
    private String poolKey;
    private String goodsClassifyInventory;
    private String goodsClassifyPrice;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public GoodsClassifyPoolBean() {
    }


    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getPoolKey() {
        return poolKey;
    }

    public void setPoolKey(String poolKey) {
        this.poolKey = poolKey;
    }

    public String getGoodsClassifyInventory() {
        return goodsClassifyInventory;
    }

    public void setGoodsClassifyInventory(String goodsClassifyInventory) {
        this.goodsClassifyInventory = goodsClassifyInventory;
    }

    public String getGoodsClassifyPrice() {
        return goodsClassifyPrice;
    }

    public void setGoodsClassifyPrice(String goodsClassifyPrice) {
        this.goodsClassifyPrice = goodsClassifyPrice;
    }
}