//package cn.com.szw.lib.myframework.imageloder;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.Uri;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.lzy.imagepicker.loader.ImageLoader;
//
//import java.io.File;
//
//import cn.com.szw.lib.myframework.R;
//
//
//public class GlideImageLoader implements ImageLoader {
//
//    @Override
//    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(R.mipmap.loading_1)           //设置错误图片
//                .placeholder(R.mipmap.loading_1)     //设置占位图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
//    }
//
//    @Override
//    public void clearMemoryCache() {
//    }
//
//    public static void glideDisplayImage(Context c, String path, int errorIcon, ImageView
//            imageView) {
//        Glide.with(c)                             //配置上下文
//                .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(errorIcon)           //设置错误图片
//                .placeholder(errorIcon)     //设置占位图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
//    }
//}