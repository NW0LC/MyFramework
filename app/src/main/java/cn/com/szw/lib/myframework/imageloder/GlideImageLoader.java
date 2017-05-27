package cn.com.szw.lib.myframework.imageloder;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

import cn.com.szw.lib.myframework.R;


public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .into(imageView).onLoadFailed(ContextCompat.getDrawable(activity,R.mipmap.loading_1));
    }

    @Override
    public void clearMemoryCache() {
    }

    public static void glideDisplayImage(Context c, String path, int errorIcon, ImageView imageView) {
        Glide.with(c)
                .load(path)
                .into(imageView).onLoadFailed(ContextCompat.getDrawable(c,errorIcon));
    }
}