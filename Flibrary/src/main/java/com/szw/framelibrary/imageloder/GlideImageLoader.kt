package com.szw.framelibrary.imageloder


import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.lzy.imagepicker.loader.ImageLoader
import com.szw.framelibrary.R

import java.io.File

class GlideImageLoader : ImageLoader {

    override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {

        Glide.with(activity)
                .load(Uri.fromFile(File(path)))
                .into(imageView).onLoadFailed(ContextCompat.getDrawable(activity, R.mipmap.loading_1))
    }

    override fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity)
                .load(Uri.fromFile(File(path)))
                .into(imageView).onLoadFailed(ContextCompat.getDrawable(activity, R.mipmap.loading_1))
    }

    override fun clearMemoryCache() {}

    companion object {

        fun glideDisplayImage(c: Context, path: String, errorIcon: Int, imageView: ImageView) {
            Glide.with(c)
                    .load(path)
                    .into(imageView).onLoadFailed(ContextCompat.getDrawable(c, errorIcon))
        }
    }
}