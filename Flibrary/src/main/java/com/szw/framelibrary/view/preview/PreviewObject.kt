package com.szw.framelibrary.view.preview

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 史忠文
 * on 2017/6/9.
 */

open class PreviewObject : AbsImg, Parcelable {

    /**
     * @return   设置缩略图
     */
    override val thumbnailUrl: String
        get() = ""
    /**
     * @return   设置正常图
     */
    override val normalUrl: String
        get() = ""
    /**
     * @return   设置大图
     */
    override val largeUrl: String
        get() = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    constructor() {}

    protected constructor(`in`: Parcel) {}

    companion object {
        val CREATOR: Parcelable.Creator<PreviewObject> = object : Parcelable.Creator<PreviewObject> {
            override fun createFromParcel(`in`: Parcel): PreviewObject {
                return PreviewObject(`in`)
            }

            override fun newArray(size: Int): Array<PreviewObject?> {
                return arrayOfNulls(size)
            }
        }
    }

}
