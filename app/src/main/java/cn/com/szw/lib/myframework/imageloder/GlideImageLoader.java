package cn.com.szw.lib.myframework.imageloder;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

import cn.com.szw.lib.myframework.R;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(Uri.fromFile(new File(path)))
                .error(R.mipmap.loading_1)
                .placeholder(R.mipmap.loading_1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

    public static void glideDisplayImage(Context c, String path, int errorIcon, ImageView imageView) {
        Glide.with(c)
                .load(path)
                .error(errorIcon)
                .placeholder(errorIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}