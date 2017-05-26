package cn.com.szw.lib.myframework.config;
/**
 * 存在手机内的信息
 * @author swz
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
    private static final String DATA_CONTEXT = "dataContext";
    private static final String DOWN_TIMER = "down_timer";

    /**
     * @param accountKey 账号
    * @param accountValue 密码
    */
    public static void saveAccount(Context context,String accountKey, String accountValue){

        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("accountKey", accountKey);
        editor.putString("accountValue", accountValue);
        editor.apply();
    }

    /**
     * @param context  上下文
     * @param autoLoginToken 自动登录秘钥
     */
    public static void saveAutoLoginToken(Context context,String autoLoginToken){

        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("autoLoginToken", autoLoginToken);
        editor.apply();
    }

    /**
     * @return  获取自动登录秘钥
     */
    public static String getAutoLoginToken(Context context) {
        String accountKey;
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        accountKey=preferences.getString("autoLoginToken",null);
        return accountKey;
    }
    /**
     * @return  获取账户名
     */
    public static String getAccountKey(Context context) {
        String accountKey;
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        accountKey=preferences.getString("accountKey",null);
        return accountKey;
    }
    /**
     * @return  获取密码
     */
    public static String getAccountValue(Context context) {
        String accountValue;
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        accountValue=preferences.getString("accountValue",null);
        return accountValue;
    }


    /**
     * @param city 城市
     */
    public static void saveCity(Context context,String city){
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("city", city);
        editor.apply();
    }

    /**
     * @return 获取城市
     */
    public static String getCity(Context context) {
        String pass;
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        pass=preferences.getString("city",null);
        return pass;
    }

    /** 保存数据
     * @param key  键
     * @param value 值
     */
    public static void saveData(Context context,String key,String value){
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**获取数据
     * @param key 键
     * @return 值
     */
    public static String getData(Context context,String key) {
        String String;
        SharedPreferences preferences = context.getSharedPreferences(DATA_CONTEXT, Context.MODE_PRIVATE);
        String=preferences.getString(key,null);
        return String;
    }


    /**
     * @param context 上下文
     * @param time  当前时间
     */
    public static void setDownTimer(Context context,String key, long time) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = context.getSharedPreferences(DOWN_TIMER,
                Context.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putLong(key, time);
        //提交当前数据
        editor.apply();
    }

    /**
     * @param context  上下文
     * @return 保存时间
     */
    public static long getDownTimer(Context context,String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = context.getSharedPreferences(DOWN_TIMER,
                Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }
}
