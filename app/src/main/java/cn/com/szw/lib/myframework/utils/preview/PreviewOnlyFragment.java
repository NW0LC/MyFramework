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
import uk.co.senab.photoview.PhotoView;


public class PreviewOnlyFragment extends MyBaseFragment {
    private static final String ARG_PARAM1 = "img_url";

    private String img_url;// 图片路径
    private int c;// 点击事件判断
    private Bitmap bp;
    private LoadImageLister loadImageLister;

    public PreviewOnlyFragment() {
    }

    // 传入Listener
    public void initListener(LoadImageLister loadImageLister) {
        this.loadImageLister = loadImageLister;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            img_url = getArguments().getString(ARG_PARAM1);
            c = getArguments().getInt("click");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_preview_only, container, false);
        PhotoView photoview = (PhotoView) rootView.findViewById(R.id.preview_only);
        photoview.setZoomable(true);// 设置photoview可以放大

        photoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c == 1) {
                    PreviewOnlyActivity.previewOnlyActivity.click();
                }

                if (c == 2) {
                    getActivity().finish();
                }
            }
        });// 自定义方法向photoview传入activity点击关闭

        // 加载图片
        GlideApp.with(getActivity()).load(img_url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (loadImageLister != null)
                    loadImageLister.failed();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                bp = ImageUtils.drawable2Bitmap(resource);
                if (loadImageLister != null)
                    loadImageLister.complete();
                return false;
            }
        }).into(photoview);
        return rootView;

    }

    /**
     * 保存图片
     */
    public void saveImage() {
        Utils.saveCroppedImage(getActivity(), bp,
                img_url.split("/")[img_url.split("/").length - 1]);
    }


    public interface LoadImageLister {
        void complete();

        void failed();
    }

}
