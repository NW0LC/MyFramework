package com.szw.framelibrary.view.photoview

import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView

class Info(// 内部图片在整个窗口的位置
        var mRect: RectF = RectF(), // 控件在窗口的位置
        var mLocalRect: RectF = RectF(),
        var mImgRect: RectF = RectF(),
        var mWidgetRect: RectF = RectF(),
        var mScale: Float = 0.toFloat(),
        var mDegrees: Float = 0.toFloat(),
        var mScaleType: ImageView.ScaleType ?= ImageView.ScaleType.CENTER_INSIDE) : Parcelable {
    constructor(parcel: Parcel) : this() {
        mRect = parcel.readParcelable(RectF::class.java.classLoader)
        mLocalRect = parcel.readParcelable(RectF::class.java.classLoader)
        mImgRect = parcel.readParcelable(RectF::class.java.classLoader)
        mWidgetRect = parcel.readParcelable(RectF::class.java.classLoader)
        mScale = parcel.readFloat()
        mDegrees = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(mRect, flags)
        parcel.writeParcelable(mLocalRect, flags)
        parcel.writeParcelable(mImgRect, flags)
        parcel.writeParcelable(mWidgetRect, flags)
        parcel.writeFloat(mScale)
        parcel.writeFloat(mDegrees)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Info> {
        override fun createFromParcel(parcel: Parcel): Info {
            return Info(parcel)
        }

        override fun newArray(size: Int): Array<Info?> {
            return arrayOfNulls(size)
        }
    }


}
