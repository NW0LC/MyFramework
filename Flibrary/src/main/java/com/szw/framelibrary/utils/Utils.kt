package com.szw.framelibrary.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

/**
 * 常用方法的工具类
 *
 * @author zhn
 */
object Utils {
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    //        DisplayMetrics displayMetrics = new DisplayMetrics();
    //        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    //        PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ((displayMetrics.widthPixels)/100)*8);
    //        params.setMargins(0,DialogUtils.getStatusHeight(this),0,0);
    //        relativeLayout1.setLayoutParams(params);
    //    }

    /**
     * @param price 金钱
     * @return 去掉.00
     */
    fun price(price: String): String {
        return price.replace(".00", "")
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    fun stringFilter(str: String): String {
        var str = str
        str = str.replace("【".toRegex(), "[").replace("】".toRegex(), "]")
                .replace("！".toRegex(), "!").replace("：".toRegex(), ":")// 替换中文标号
        val regEx = "[『』]" // 清除掉特殊字符
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.replaceAll("").trim { it <= ' ' }
    }

    /**
     * @param path
     * @return
     * @throws Exception
     * 绝对路径转base 64
     */
    @Throws(Exception::class)
    fun encodeBase64File(path: String): String {
        val file = File(path)
        val inputFile = FileInputStream(file)
        val buffer = ByteArray(file.length().toInt())
        inputFile.read(buffer)
        inputFile.close()
        return Base64.encodeToString(buffer, Base64.DEFAULT)
    }


    /**
     * @param sourceStr
     * @return md5加密 32位小写
     */
    fun getMD5(sourceStr: String): String {
        var result = ""
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(sourceStr.toByteArray())
            val b = md.digest()
            var i: Int
            val buf = StringBuffer("")
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0)
                    i += 256
                if (i < 16)
                    buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            result = buf.toString()
            println("result: " + result)//32位的加密
            //            System.out.println("result: " +
            //                    buf.toString().substring(8, 24));//16位的加密
        } catch (e: NoSuchAlgorithmException) {
            //TODO Auto-generated catch block e.printStackTrace();
        }

        return result
    }

    /**
     * 集合转化为base64 字符串
     *
     * @param <T>
     * @param SceneList
     * @return
     * @throws IOException
    </T> */
    @Throws(IOException::class)
    fun <T> SceneList2String(SceneList: List<T>): String {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 然后将得到的字符数据装载到ObjectOutputStream
        val objectOutputStream = ObjectOutputStream(
                byteArrayOutputStream)
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList)
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        val SceneListString = String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT))
        // 关闭objectOutputStream
        objectOutputStream.close()
        return SceneListString

    }

    fun showImg(url: String, iv: SimpleDraweeView) {
        val controller = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .setAutoPlayAnimations(true)
                .build()
        iv.controller = controller
    }

    /**
     * base64 字符串转化为集合
     *
     * @param <T>
     * @param <T>
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
    </T></T> */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun <T> String2SceneList(SceneListString: String): List<T> {
        val mobileBytes = Base64.decode(SceneListString.toByteArray(),
                Base64.DEFAULT)
        val byteArrayInputStream = ByteArrayInputStream(
                mobileBytes)
        val objectInputStream = ObjectInputStream(
                byteArrayInputStream)
        val SceneList = objectInputStream.readObject() as List<T>
        objectInputStream.close()
        return SceneList
    }


    /**
     * bitmap存入本地
     */
    fun saveCroppedImage(context: Context, bmp: Bitmap, name: String) {
        val path = File(Environment.getExternalStorageDirectory().absolutePath + "/${context.packageName}")
        if (!path.exists())
            path.mkdirs()

        val file = File(path, name + ".png")

        if (file.exists()) {

            Toast.makeText(context, "保存成功" + Environment.getExternalStorageDirectory().absolutePath + "/cache", Toast.LENGTH_SHORT).show()
            return
        }


        try {
            file.createNewFile()
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            Toast.makeText(context, "保存成功" + Environment.getExternalStorageDirectory().absolutePath + "/itemei", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "保存失败" + Environment.getExternalStorageDirectory().absolutePath + "/cache", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * bitmap存入本地
     */
    fun saveCroppedImage(bmp: Bitmap, name: String) {
        val path = File(Environment.getExternalStorageDirectory().absolutePath + "/itemei/cache")
        if (!path.exists())
            path.mkdirs()

        val file = File(path, name)

        if (file.exists()) {

            return
        }
        try {
            file.createNewFile()
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
        }

    }


    /**
     * 获取压缩之后的file路径
     */
    fun getCroppedImage(url: String): String {
        val path = Environment.getExternalStorageDirectory().absolutePath + "/itemei/cache/" + url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1]
        val file = File(path)
        return if (file.exists())
            path
        else
            url
    }

    fun startActivity(mContext: Context, mClass: Class<*>) {
        val intent = Intent(mContext, mClass)
        mContext.startActivity(intent)
    }

    fun startActivityForResult(mContext: Activity, mClass: Class<*>, requestCode: Int) {
        val intent = Intent(mContext, mClass)
        mContext.startActivityForResult(intent, requestCode)
    }


    /**
     * 状态栏高度算法
     *
     * @param activity
     * @return
     */
    fun getStatusHeight(activity: Activity): Int {
        var statusHeight = 0
        val localRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        statusHeight = localRect.top
        if (0 == statusHeight) {
            val localClass: Class<*>
            try {
                localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString())
                statusHeight = activity.resources.getDimensionPixelSize(i5)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }


    /**
     * 删除文件
     *
     * @param files
     */
    fun deleteFile(files: List<File>) {
        for (file in files) {
            if (file.exists()) { // 判断文件是否存在
                if (file.isFile) { // 判断是否是文件
                    file.delete() // 删除文件;
                }
            }
        }
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    fun RecursionDeleteFile(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.isEmpty()) {
                file.delete()
                return
            }
            for (f in childFile) {
                RecursionDeleteFile(f)
            }
            file.delete()
        }
    }

    /**
     * 验证手机格式
     */
    fun isMobileNO(mobiles: String): Boolean {
        /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        val telRegex = "[1][358]\\d{9}"//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobiles))
            false
        else
            mobiles.matches(telRegex.toRegex())
    }

    //此方法只是关闭软键盘
    fun hintKbTwo(context: Activity) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && context.currentFocus != null) {
            if (context.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}
