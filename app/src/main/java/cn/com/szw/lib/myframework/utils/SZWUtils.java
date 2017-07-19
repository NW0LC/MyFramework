package cn.com.szw.lib.myframework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.szw.lib.myframework.R;
import cn.com.szw.lib.myframework.app.MyApplication;
import cn.com.szw.lib.myframework.observer.SmsContentObserver;

import static android.content.ContentValues.TAG;


/**
 * Created by 史忠文
 * on 2017/3/23.
 */

public class SZWUtils {


    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    public static boolean CheckLogin(Activity mContext,@NonNull Intent intent,Class clazz) {
        if (!MyApplication.checkUserLogin()) {
            Intent login = new Intent(mContext, clazz);
            login.putExtras(intent);
            mContext.startActivityForResult(login, 0xc8);
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
            return false;
        } else
            return true;
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    public static boolean CheckLogin(Activity mContext,Class clazz) {
        if (!MyApplication.checkUserLogin()) {
            Intent login = new Intent(mContext, clazz);
            mContext.startActivityForResult(login, 0xc8);
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
            return false;
        } else
            return true;
    }

    /**
     * @param mContext 上下文
     * @return true登录
     */
    public static boolean CheckLogin(Fragment mContext,Class clazz) {
        if (!MyApplication.checkUserLogin()) {
            Intent login = new Intent(mContext.getActivity(),clazz);
            mContext.startActivityForResult(login, 0xc8);
            mContext.getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
            return false;
        } else
            return true;
    }

    /**
     * 获取单个App版本号
     **/
    public static String getAppVersion(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageManager pManager = context.getPackageManager();
        PackageInfo packageInfo = pManager.getPackageInfo(packageName, 0);
        return packageInfo.versionName;
    }
    /**
     *
     * @param versionServer versionServer
     * @param versionLocal versionLocal
     * @return if version1 > version2, return 1, if equal, return 0, else return
     *         -1
     */
    public static int VersionComparison(String versionServer, String versionLocal) {
        if (versionServer == null || versionServer.length() == 0 || versionLocal == null || versionLocal.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < versionServer.length() && index2 < versionLocal.length()) {
            int[] number1 = getValue(versionServer, index1);
            Log.i(TAG," ===== number1 ====" + Arrays.toString(number1));
            int[] number2 = getValue(versionLocal, index2);
            Log.i(TAG," ===== number2 ====" + Arrays.toString(number2));

            if (number1[0] < number2[0]){
                Log.i(TAG," ===== number1[0] ====" + number1[0]);
                Log.i(TAG," ===== number2[0] ====" + number2[0]);
                return -1;
            }
            else if (number1[0] > number2[0]){
                Log.i(TAG," ===== number1[0] ====" + number1[0]);
                Log.i(TAG," ===== number2[0] ====" + number2[0]);
                return 1;
            }
            else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == versionServer.length() && index2 == versionLocal.length())
            return 0;
        if (index1 < versionServer.length())
            return 1;
        else
            return -1;
    }

    /**
     *
     * @param version
     * @param index
     *            the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    /**
     * @param mContext 上下文
     * @param textView 返回验证码的textView
     * @return 验证码handler
     */
    public static Handler patternCode(Context mContext, TextView textView) {
        return new MyHandler(mContext, textView);
    }

    private static class MyHandler extends Handler {
        Context mContext;
        TextView textView;

        private MyHandler(Context mContext, TextView textView) {
            this.mContext = mContext;
            this.textView = textView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String outbox = (String) msg.obj;
//            edCode.setText(outbox);
//            Toast.makeText(mContext, outbox, Toast.LENGTH_SHORT).show();
            textView.setText(SZWUtils.patternCode(outbox,4));
        }
    }


    /**
     * 注册读取短信observer
     *
     * @param context  上下文
     * @param mHandler 监听
     * @return observer
     */
    public static SmsContentObserver registerSMS(Context context, Handler mHandler) {
        //注册内容观察者获取短信
        SmsContentObserver smsContentObserver = new SmsContentObserver(context, mHandler);
        // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms
        // 监听不到其他的Uri 比如说 content://sms/outbox
        Uri smsUri = Uri.parse("content://sms");
        context.getContentResolver().registerContentObserver(smsUri, true, smsContentObserver);
        return smsContentObserver;
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent 短信
     * @return 验证码
     */
    private static String patternCode(String patternContent) {
        String patternCoder = "(?<!\\d)\\d{7}(?!\\d)";//匹配6位验证码
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    /**
     * 从短信字符窜提取验证码
     * @param body 短信内容
     * @param length  验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    public static String patternCode(String body, int length) {
        // 首先([a-zA-Z0-9]{length})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{length})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{length})后面不能有数字出现


//  获得数字字母组合
//    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

//  获得纯数字
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + length+ "})(?![0-9])");

        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext    上下文
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * @param index 间距
     * @param strs  字符串
     * @param str   插入字符
     * @return 插入字符后字符串
     */
    public static String insert(int index, String strs, String str) {
        String len = "";
        if (strs.length() > index) {
            int oldIndex = 0;
            for (int i = index; i < strs.length(); i += index) {
                len += strs.substring(oldIndex, i) + str;
                oldIndex = i;
            }

            if ((oldIndex + index) >= strs.length())
                len += strs.substring(oldIndex);
        }
        return len;
    }

    /**
     * @param color  需改变字体颜色
     * @param text    所有文字
     * @param keyword  需变色的文字
     * @return
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        String string = text.toLowerCase();
        String key = keyword.toLowerCase();
        Pattern pattern = Pattern.compile(key);
        Matcher matcher = pattern.matcher(string);
        SpannableString ss = new SpannableString(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }


    /**
     * 指定 文字 指定 颜色 变色
     */
    public static CharSequence setColor(Context context, String text, String text1, String text2) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
// 关键字“孤舟”变色，0-text1.length()
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary)), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
// 关键字“寒江雪”变色，text1.length() + 6-text1.length() + 6 + text2.length()
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), text1.length() + 6, text1.length() + 6 + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * @param html 富文本
     * @return  矫正图片后的富文本
     */
    public static String getHtmlContent(String html){
//
        Document doc_Dis = Jsoup.parse(html);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0){
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "max-width:100%;height:auto;");
            }
        }
//
        return doc_Dis.toString();
    }
}
