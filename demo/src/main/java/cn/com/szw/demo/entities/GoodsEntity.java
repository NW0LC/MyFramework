package cn.com.szw.demo.entities;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 史忠文
 * on 2017/4/17.
 */

public class GoodsEntity extends RealmObject {
    public static final String primaryKey = "goodsId";
    public static final String DateKey = "date";
    /**
     * goodsId :
     * goodsCarId :购物车内商品Id
     * goodsName : 商品
     * goodsImg : 商品图片
     * goodsPrice : ￥16.8
     * soldCount : 已售数量
     * goodsMark:"商品标签"
     * isCollection : 1：未收藏，2：已收藏
     * goodsType : 1：正常商品，2：抢购未开场，3：抢购已开场未结束 4：已下架
     * goodsBanner : [{"address":"","ImgUrl":"","title":""}]
     * goodsOldPrice :
     * goodsFullMail
     * goodsStartTime":"商品开始时间",
     * goodsDownTime : 倒计时，秒
     * goodsExpressPrice : 快递价格
     * goodsMonthSoldCount : 月销量
     * goodsAddress : 商品底
     * goodsDetailUrl : 详情页地址
     * goodsInventory : 总库存
     * goodsClassify : [{"goodsClassifyName":"颜色","goodsClassifyRealmList":[{"goodsSubClassifyName":"黑色","goodsSubClassifyId":"1","goodsSubClassifyUrl":"","goodsSubClassifyNextId":"1,2,3,4"}]}]
     * goodsClassifyPool : [{"poolId":"","poolkey":"","goodsClassifyInventory":"分类库存","goodsClassifyPrice":"分类价格"}]
     * goodsCount : 数量
     * goodsChooseClassify : 商品已选分类：蓝色 草莓味 500g 饼干
     *"goodsCollectionPriceChangeInfo":"商品收藏价格变化信息",
     */



    @PrimaryKey
    private String goodsId;
    private String goodsCarId;
    private String goodsName;
    private String goodsImg;
    private String goodsPrice;
    private String soldCount;
    private String goodsMark;
    private Date date;
    private String isCollection;
    private String goodsType;
    private String goodsOldPrice;
    private String goodsStartTime;
    private String goodsDownTime;
    private String goodsExpressPrice;
    private String goodsMonthSoldCount;
    private String goodsAddress;
    private String goodsDetailUrl;
    private String goodsInventory;
    private String goodsFullMail;
    private boolean isCheck;
    private RealmList<BannersBean> goodsBanner;
    private RealmList<GoodsClassifyBean> goodsClassify;
    private RealmList<GoodsClassifyPoolBean> goodsClassifyPool;
    private String goodsCount;
    private String goodsChooseClassify;
    private String goodsCollectionPriceChangeInfo;

    public String getGoodsCarId() {
        return goodsCarId;
    }

    public void setGoodsCarId(String goodsCarId) {
        this.goodsCarId = goodsCarId;
    }

    public String getGoodsStartTime() {
        return goodsStartTime;
    }

    public void setGoodsStartTime(String goodsStartTime) {
        this.goodsStartTime = goodsStartTime;
    }

    public String getGoodsCollectionPriceChangeInfo() {
        return goodsCollectionPriceChangeInfo;
    }

    public void setGoodsCollectionPriceChangeInfo(String goodsCollectionPriceChangeInfo) {
        this.goodsCollectionPriceChangeInfo = goodsCollectionPriceChangeInfo;
    }

    public String getGoodsMark() {
        return goodsMark;
    }

    public void setGoodsMark(String goodsMark) {
        this.goodsMark = goodsMark;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGoodsFullMail() {
        return goodsFullMail;
    }

    public void setGoodsFullMail(String goodsFullMail) {
        this.goodsFullMail = goodsFullMail;
    }

    public RealmList<BannersBean> getGoodsBanner() {
        return goodsBanner;
    }

    public void setGoodsBanner(RealmList<BannersBean> goodsBanner) {
        this.goodsBanner = goodsBanner;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(String soldCount) {
        this.soldCount = soldCount;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsOldPrice() {
        return goodsOldPrice;
    }

    public void setGoodsOldPrice(String goodsOldPrice) {
        this.goodsOldPrice = goodsOldPrice;
    }

    public String getGoodsDownTime() {
        return goodsDownTime;
    }

    public void setGoodsDownTime(String goodsDownTime) {
        this.goodsDownTime = goodsDownTime;
    }

    public String getGoodsExpressPrice() {
        return goodsExpressPrice;
    }

    public void setGoodsExpressPrice(String goodsExpressPrice) {
        this.goodsExpressPrice = goodsExpressPrice;
    }

    public String getGoodsMonthSoldCount() {
        return goodsMonthSoldCount;
    }

    public void setGoodsMonthSoldCount(String goodsMonthSoldCount) {
        this.goodsMonthSoldCount = goodsMonthSoldCount;
    }

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }

    public String getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(String goodsInventory) {
        this.goodsInventory = goodsInventory;
    }


    public RealmList<GoodsClassifyBean> getGoodsClassify() {
        return goodsClassify;
    }

    public void setGoodsClassify(RealmList<GoodsClassifyBean> goodsClassify) {
        this.goodsClassify = goodsClassify;
    }

    public RealmList<GoodsClassifyPoolBean> getGoodsClassifyPool() {
        return goodsClassifyPool;
    }

    public void setGoodsClassifyPool(RealmList<GoodsClassifyPoolBean> goodsClassifyPool) {
        this.goodsClassifyPool = goodsClassifyPool;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsChooseClassify() {
        return goodsChooseClassify;
    }

    public void setGoodsChooseClassify(String goodsChooseClassify) {
        this.goodsChooseClassify = goodsChooseClassify;
    }

    public  interface  OnClassifyChange{
    void onChange(String tag);
}
}
