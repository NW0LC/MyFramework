package cn.com.szw.lib.myframework.view.photoview;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by liuheng on 2015/8/19.
 */
public class Info implements Parcelable {
    // 内部图片在整个窗口的位置
    RectF mRect = new RectF();
    // 控件在窗口的位置
    RectF mLocalRect = new RectF();
    RectF mImgRect = new RectF();
    RectF mWidgetRect = new RectF();
    float mScale;
    float mDegrees;
    ImageView.ScaleType mScaleType;

    public Info(RectF rect, RectF local, RectF img, RectF widget, float scale, float degrees, ImageView.ScaleType scaleType) {
        mRect.set(rect);
        mLocalRect.set(local);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
    }

    protected Info(Parcel in) {
        mRect = in.readParcelable(RectF.class.getClassLoader());
        mLocalRect = in.readParcelable(RectF.class.getClassLoader());
        mImgRect = in.readParcelable(RectF.class.getClassLoader());
        mWidgetRect = in.readParcelable(RectF.class.getClassLoader());
        mScale = in.readFloat();
        mDegrees = in.readFloat();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mRect, flags);
        dest.writeParcelable(mLocalRect, flags);
        dest.writeParcelable(mImgRect, flags);
        dest.writeParcelable(mWidgetRect, flags);
        dest.writeFloat(mScale);
        dest.writeFloat(mDegrees);
    }
}
