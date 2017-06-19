package cn.com.szw.demo;

import io.realm.RealmObject;

public class BannersBean extends RealmObject {
    /**
     * title : 抬头
     * type : 0：店铺，1：商品，2：广告
     * id : 店铺、商品id
     * imgUrl : 图片地址
     * adUrl : 广告微页面(暂时为空,以后版本若有则传微页面地址)
     */

    private String title;
    private String type;
    private String id;
    private String imgUrl;
    private String adUrl;

    public BannersBean() {
    }

    public BannersBean(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}