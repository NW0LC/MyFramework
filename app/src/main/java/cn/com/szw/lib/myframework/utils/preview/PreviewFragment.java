package cn.com.szw.lib.myframework.utils.preview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.base.MyBaseFragment;
import cn.com.szw.lib.myframework.imageloder.GlideApp;
import cn.com.szw.lib.myframework.utils.Utils;
import cn.com.szw.lib.myframework.view.photoview.PhotoView;


/**
 * Created by 史忠文
 * on 2017/6/9.
 */

public class PreviewFragment extends MyBaseFragment implements View.OnClickListener {
    private  static final String Arg_NormalUrl = "Arg_NormalUrl";
    private  static final String Arg_ThumbnailUrl = "Arg_ThumbnailUrl";
    private LoadImageLister loadImageLister;
    private Bitmap bitmap;
    private String imgUrl;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_preview, container, false);
        }
        return rootView;
    }

    public static PreviewFragment Instance(String NormalUrl,String ThumbnailUrl){
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putString(Arg_NormalUrl,NormalUrl);
        args.putString(Arg_ThumbnailUrl,ThumbnailUrl);
        previewFragment.setArguments(args);
        return previewFragment;
    }
    @Override
    public void initView() {
        imgUrl = getArguments().getString(Arg_NormalUrl);
        PhotoView photoView = (PhotoView) rootView.findViewById(R.id.photoView);
        photoView.setOnClickListener(this);
        photoView.enable();//设置可缩放
        // 加载图片
        // setup Glide request without the into() method
        GlideApp.with(getContext()).load(imgUrl).thumbnail(GlideApp.with(this).load(getArguments().getString(Arg_ThumbnailUrl))).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (loadImageLister != null)
                    loadImageLister.failed();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                bitmap = ImageUtils.drawable2Bitmap(resource);
                if (loadImageLister != null)
                    loadImageLister.complete();
                return false;
            }
        }).into(photoView); // 加载图片
    }
    /**
     * 保存图片
     */
    public void saveImage() {
        Utils.saveCroppedImage(getActivity(), bitmap,
                imgUrl.split("/")[imgUrl.split("/").length - 1]);
    }

    public void setLoadImageLister(LoadImageLister loadImageLister) {
        this.loadImageLister = loadImageLister;
    }
    @Override
    public void onClick(View v) {
        if (loadImageLister != null)
            loadImageLister.onClick();
    }

    interface LoadImageLister {
        void complete();
        void failed();
        void onClick();
    }
}
