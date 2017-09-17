package com.szw.framelibrary.view.preview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.szw.framelibrary.R
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.Utils
import com.szw.framelibrary.view.photoview.PhotoView


/**
 * Created by 史忠文
 * on 2017/6/9.
 */

class PreviewFragment : MyBaseFragment(), View.OnClickListener {
    private var loadImageLister: LoadImageLister? = null
    lateinit var bitmap: Bitmap
    private var imgUrl: String=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_preview, container, false)
        return rootView
    }

    override fun initView() {
        imgUrl = arguments.getString(Arg_NormalUrl)
        val photoView = rootView.findViewById<PhotoView>(R.id.photoView)
        photoView.setOnClickListener(this)
        photoView.enable()//设置可缩放
        // 加载图片
//         setup Glide request without the into() method
        Glide.with(context).load(imgUrl).thumbnail(Glide.with(this).load(arguments.getString(Arg_ThumbnailUrl))).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                if (loadImageLister != null)
                    loadImageLister?.failed()
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                bitmap = ImageUtils.drawable2Bitmap(resource)
                if (loadImageLister != null)
                    loadImageLister?.complete()
                return false
            }
        }).into(photoView) // 加载图片
    }

    /**
     * 保存图片
     */
    fun saveImage() {
        Utils.saveCroppedImage(activity, bitmap, imgUrl.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[imgUrl.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1])
    }

    fun setLoadImageLister(loadImageLister: LoadImageLister) {
        this.loadImageLister = loadImageLister
    }

    override fun onClick(v: View) {
        if (loadImageLister != null)
            loadImageLister?.onClick()
    }

    interface LoadImageLister {
        fun complete()
        fun failed()
        fun onClick()
    }

    companion object {
        private val Arg_NormalUrl = "Arg_NormalUrl"
        private val Arg_ThumbnailUrl = "Arg_ThumbnailUrl"

        fun Instance(NormalUrl: String, ThumbnailUrl: String): PreviewFragment {
            val previewFragment = PreviewFragment()
            val args = Bundle()
            args.putString(Arg_NormalUrl, NormalUrl)
            args.putString(Arg_ThumbnailUrl, ThumbnailUrl)
            previewFragment.arguments = args
            return previewFragment
        }
    }
}
