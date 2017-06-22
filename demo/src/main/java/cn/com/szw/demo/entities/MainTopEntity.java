package cn.com.szw.demo.entities;

import java.util.List;

/**
 * Created by 史忠文
 * on 2017/4/17.
 */

public class MainTopEntity {
    private List<BannersBean> banner;
    private List<MainNewsEntity> news;
    private String newsUrl;
    private List<BannersBean> advertisement;
    private List<CarnivalBean> carnival;
    private List<GoodGoodsBean> goodGoods;
    private List<BannersBean> advertisement2;
    private List<GoodsEntity> recommend;

    public List<MainNewsEntity> getNews() {
        return news;
    }

    public void setNews(List<MainNewsEntity> news) {
        this.news = news;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public List<BannersBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannersBean> banner) {
        this.banner = banner;
    }


    public List<BannersBean> getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(List<BannersBean> advertisement) {
        this.advertisement = advertisement;
    }

    public List<CarnivalBean> getCarnival() {
        return carnival;
    }

    public void setCarnival(List<CarnivalBean> carnival) {
        this.carnival = carnival;
    }

    public List<GoodGoodsBean> getGoodGoods() {
        return goodGoods;
    }

    public void setGoodGoods(List<GoodGoodsBean> goodGoods) {
        this.goodGoods = goodGoods;
    }

    public List<BannersBean> getAdvertisement2() {
        return advertisement2;
    }

    public void setAdvertisement2(List<BannersBean> advertisement2) {
        this.advertisement2 = advertisement2;
    }

    public List<GoodsEntity> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<GoodsEntity> recommend) {
        this.recommend = recommend;
    }



    public static class CarnivalBean {
        /**
         * goodsId :
         * title :
         * goodsImg : 商品图片
         * info :
         */

        private String goodsId;
        private String title;
        private String goodsImg;
        private String info;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public static class GoodGoodsBean {
        /**
         * id : 0
         * title : 服装城
         * discount : 4.9折（第一个商品才有）
         * subTitle : 鞋包买3免1
         * imgUrl : 图片地址
         * type : 1：商品，2：专场列表
         */

        private String id;
        private String title;
        private String discount;
        private String subTitle;
        private String imgUrl;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
