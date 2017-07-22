package cn.com.szw.demo

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast

import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter
import com.cjt2325.cameralibrary.util.DeviceUtil
import com.cjt2325.cameralibrary.util.FileUtil

import java.io.File

class CameraActivity : AppCompatActivity() {
    private var jCameraView: JCameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_camera)
        jCameraView = findViewById(R.id.jcameraview) as JCameraView
        //设置视频保存路径
        jCameraView!!.setSaveVideoPath(Environment.getExternalStorageDirectory().path + File.separator + "JCamera")
        jCameraView!!.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER)
        jCameraView!!.setTip("JCameraView Tip")
        jCameraView!!.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE)
        jCameraView!!.setErrorLisenter(object : ErrorLisenter {
            override fun onError() {
                //错误监听
                Log.i("CJT", "camera error")
                val intent = Intent()
                setResult(103, intent)
                finish()
            }

            override fun AudioPermissionError() {
                Toast.makeText(this@CameraActivity, "给点录音权限可以?", Toast.LENGTH_SHORT).show()
            }
        })
        //JCameraView监听
        jCameraView!!.setJCameraLisenter(object : JCameraLisenter {
            override fun captureSuccess(bitmap: Bitmap) {
                //获取图片bitmap
                //                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                val path = FileUtil.saveBitmap("JCamera", bitmap)
                val intent = Intent()
                intent.putExtra("path", path)
                setResult(101, intent)
                finish()
            }

            override fun recordSuccess(url: String, firstFrame: Bitmap) {
                //获取视频路径
                val path = FileUtil.saveBitmap("JCamera", firstFrame)
                Log.i("CJT", "url = $url, Bitmap = $path")
                val intent = Intent()
                intent.putExtra("path", path)
                setResult(101, intent)
                finish()
            }

            override fun quit() {
                //退出按钮
                this@CameraActivity.finish()
            }
        })

        Log.i("CJT", DeviceUtil.getDeviceModel())
    }

    override fun onStart() {
        super.onStart()
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }

    override fun onResume() {
        Log.i("CJT", "onResume")
        super.onResume()
        jCameraView!!.onResume()
    }

    override fun onPause() {
        Log.i("CJT", "onPause")
        super.onPause()
        jCameraView!!.onPause()
    }
}