package cn.com.szw.demo.entities;

import com.blankj.utilcode.util.EncodeUtils;

/**
 * Created by 史忠文
 * on 2017/6/7.
 */

public class MainNewsEntity {

    /**
     * newsInfo : 头条1
     * newsUrl :
     */

    private String newsInfo;
    private String newsUrl;

    public String getNewsInfo() {
        return EncodeUtils.urlDecode(newsInfo);
    }

    public void setNewsInfo(String newsInfo) {
        this.newsInfo = newsInfo;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
