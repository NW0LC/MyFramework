package com.szw.framelibrary.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import com.szw.framelibrary.R
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.observer.SmsContentObserver
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern


/**
 * Created by 史忠文
 * on 2017/3/23.
 */

object SZWUtils {


    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun CheckLogin(mContext: Activity, intent: Intent, clazz: Class<*>): Boolean {
        if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, clazz)
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            return false
        } else
            return true
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    fun CheckLogin(mContext: Activity, clazz: Class<*>): Boolean {
        if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, clazz)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            return false
        } else
            return true
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    fun CheckLogin(mContext: Fragment, clazz: Class<*>): Boolean {
        if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext.activity, clazz)
            mContext.startActivityForResult(login, 0xc8)
            mContext.activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            return false
        } else
            return true
    }

    /**
     * 获取单个App版本号
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppVersion(context: Context, packageName: String): String {
        val pManager = context.packageManager
        val packageInfo = pManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName
    }

    /**
     *
     * @param versionServer versionServer
     * @param versionLocal versionLocal
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    fun VersionComparison(versionServer: String?, versionLocal: String?): Int {
        if (versionServer == null || versionServer.isEmpty() || versionLocal == null || versionLocal.isEmpty())
            throw IllegalArgumentException("Invalid parameter!")

        var index1 = 0
        var index2 = 0
        while (index1 < versionServer.length && index2 < versionLocal.length) {
            val number1 = getValue(versionServer, index1)
            Log.i(TAG, " ===== number1 ====" + Arrays.toString(number1))
            val number2 = getValue(versionLocal, index2)
            Log.i(TAG, " ===== number2 ====" + Arrays.toString(number2))

            when {
                number1[0] < number2[0] -> {
                    Log.i(TAG, " ===== number1[0] ====" + number1[0])
                    Log.i(TAG, " ===== number2[0] ====" + number2[0])
                    return -1
                }
                number1[0] > number2[0] -> {
                    Log.i(TAG, " ===== number1[0] ====" + number1[0])
                    Log.i(TAG, " ===== number2[0] ====" + number2[0])
                    return 1
                }
                else -> {
                    index1 = number1[1] + 1
                    index2 = number2[1] + 1
                }
            }
        }
        if (index1 == versionServer.length && index2 == versionLocal.length)
            return 0
        return if (index1 < versionServer.length)
            1
        else
            -1
    }

    /**
     *
     * @param version
     * @param index
     * the starting point
     * @return the number between two dots, and the index of the dot
     */
    fun getValue(version: String, index: Int): IntArray {
        var index = index
        val value_index = IntArray(2)
        val sb = StringBuilder()
        while (index < version.length && version[index] != '.') {
            sb.append(version[index])
            index++
        }
        value_index[0] = Integer.parseInt(sb.toString())
        value_index[1] = index

        return value_index
    }

    /**
     * @param mContext 上下文
     * @param textView 返回验证码的textView
     * @return 验证码handler
     */
    fun patternCode(mContext: Context, textView: TextView): Handler {
        return MyHandler(mContext, textView)
    }

    class MyHandler constructor(internal var mContext: Context, private var textView: TextView) : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val outbox = msg.obj as String
            //            edCode.setText(outbox);
            //            Toast.makeText(mContext, outbox, Toast.LENGTH_SHORT).show();
            textView.text = SZWUtils.patternCode(outbox, 4)
        }
    }


    /**
     * 注册读取短信observer
     *
     * @param context  上下文
     * @param mHandler 监听
     * @return observer
     */
    fun registerSMS(context: Context, mHandler: Handler): SmsContentObserver {
        //注册内容观察者获取短信
        val smsContentObserver = SmsContentObserver(context, mHandler)
        // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms
        // 监听不到其他的Uri 比如说 content://sms/outbox
        val smsUri = Uri.parse("content://sms")
        context.contentResolver.registerContentObserver(smsUri, true, smsContentObserver)
        return smsContentObserver
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent 短信
     * @return 验证码
     */
    private fun patternCode(patternContent: String): String? {
        val patternCoder = "(?<!\\d)\\d{7}(?!\\d)"//匹配6位验证码
        if (TextUtils.isEmpty(patternContent)) {
            return null
        }
        val p = Pattern.compile(patternCoder)
        val matcher = p.matcher(patternContent)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }

    /**
     * 从短信字符窜提取验证码
     * @param body 短信内容
     * @param length  验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    fun patternCode(body: String, length: Int): String? {
        // 首先([a-zA-Z0-9]{length})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{length})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{length})后面不能有数字出现


        //  获得数字字母组合
        //    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        //  获得纯数字
        val p = Pattern.compile("(?<![0-9])([0-9]{$length})(?![0-9])")

        val m = p.matcher(body)
        if (m.find()) {
            println(m.group())
            return m.group(0)
        }
        return null
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext    上下文
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    fun isServiceWork(mContext: Context, serviceName: String): Boolean {
        var isWork = false
        val myAM = mContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList = myAM.getRunningServices(40)
        if (myList.size <= 0) {
            return false
        }
        for (i in myList.indices) {
            val mName = myList[i].service.className
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }

    /**
     * @param index 间距
     * @param strs  字符串
     * @param str   插入字符
     * @return 插入字符后字符串
     */
    fun insert(index: Int, strs: String, str: String): String {
        var len = ""
        if (strs.length > index) {
            var oldIndex = 0
            var i = index
            while (i < strs.length) {
                len += strs.substring(oldIndex, i) + str
                oldIndex = i
                i += index
            }

            if (oldIndex + index >= strs.length)
                len += strs.substring(oldIndex)
        }
        return len
    }

    /**
     * @param color  需改变字体颜色
     * @param text    所有文字
     * @param keyword  需变色的文字
     * @return
     */
    fun matcherSearchTitle(color: Int, text: String, keyword: String): SpannableString {
        val string = text.toLowerCase()
        val key = keyword.toLowerCase()
        val pattern = Pattern.compile(key)
        val matcher = pattern.matcher(string)
        val ss = SpannableString(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            ss.setSpan(ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return ss
    }


    /**
     * 指定 文字 指定 颜色 变色
     */
    fun setColor(context: Context, text: String, text1: String, text2: String): CharSequence {
        val style = SpannableStringBuilder(text)
        // 关键字“孤舟”变色，0-text1.length()
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), 0, text1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 关键字“寒江雪”变色，text1.length() + 6-text1.length() + 6 + text2.length()
        style.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), text1.length + 6, text1.length + 6 + text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return style
    }

    /**
     * @param html 富文本
     * @return  矫正图片后的富文本
     */
    fun getHtmlContent(html: String): String {
        //
        val doc_Dis = Jsoup.parse(html)
        val ele_Img = doc_Dis.getElementsByTag("img")
        if (ele_Img.size != 0) {
            for (e_Img in ele_Img) {
                e_Img.attr("style", "max-width:100%;height:auto;")
            }
        }
        //
        return doc_Dis.toString()
    }
}
