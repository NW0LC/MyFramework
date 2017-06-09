package cn.com.szw.lib.myframework.utils.preview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 史忠文
 * on 2017/6/9.
 */

public  class PreviewObject implements AbsImg, Parcelable {

    /**
     * @return   设置缩略图
     */
    @Override
    public String getThumbnailUrl() {
        return "";
    }
    /**
     * @return   设置正常图
     */
    @Override
    public String getNormalUrl() {
        return "";
    }
    /**
     * @return   设置大图
     */
    @Override
    public String getLargeUrl() {
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public PreviewObject() {
    }

    protected PreviewObject(Parcel in) {
    }
    public static final Creator<PreviewObject> CREATOR = new Creator<PreviewObject>() {
        @Override
        public PreviewObject createFromParcel(Parcel in) {
            return new PreviewObject(in);
        }

        @Override
        public PreviewObject[] newArray(int size) {
            return new PreviewObject[size];
        }
    };

}
