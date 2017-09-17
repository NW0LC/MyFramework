package com.szw.framelibrary.app


import android.content.Context
import android.support.multidex.MultiDexApplication
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.facebook.drawee.backends.pipeline.Fresco
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.szw.framelibrary.entities.AbsUser
import com.umeng.analytics.MobclickAgent
import okhttp3.OkHttpClient
import java.lang.Exception
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager


/**
 * Created by Swain
 * on 2016/6/28.
 */
abstract class MyApplication : MultiDexApplication(), AbsApplication {

    /**
     * 本地存储
     */
    private var spUtils: SPUtils? = null

    fun init() {
        salt = getSaltStr()?:""
        Fresco.initialize(this)
        Utils.init(this)
        UmengInit(this)
        spUtils = SPUtils.getInstance(packageName)
        initOkGo()
        //Install  程序崩溃日志初始化
        //        CustomActivityOnCrash.install(this);
    }

    private fun UmengInit(myApplication: MyApplication) {
        MobclickAgent.setScenarioType(myApplication, MobclickAgent.EScenarioType.E_UM_NORMAL)
        MobclickAgent.setDebugMode(true)
    }

    private fun initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        //        HttpHeaders headers = new HttpHeaders();
        //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //        headers.put("commonHeaderKey2", "commonHeaderValue2");
        //        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        val builder = OkHttpClient.Builder()
        //log相关
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO)                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor)
        //builder.addInterceptor(new ChuckInterceptor(this));                       //第三方的开源库，使用通知显示当前请求的log

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(CookieJarImpl(DBCookieStore(this)))              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        //方法二：自定义信任规则，校验服务端证书
        val sslParams2 = HttpsUtils.getSslSocketFactory(SafeTrustManager())
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(SafeHostnameVerifier())

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE).retryCount = 3                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        //                .addCommonHeaders(headers)                      //全局公共头
        //                .addCommonParams(params);                       //全局公共参数
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private inner class SafeTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            try {
                for (certificate in chain) {
                    certificate.checkValidity() //检查证书是否过期，签名是否通过等
                }
            } catch (e: Exception) {
                throw CertificateException(e)
            }

        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private inner class SafeHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true
        }
    }

    companion object {
        /**
         * 加盐
         */
        var salt = ""


        fun getSPUtils(context: Context): SPUtils? {
            val application = context.applicationContext as MyApplication
            return application.spUtils
        }

        //===================================================================================================================
        private var user: AbsUser? = null


        //判断 用户是否登录
        fun checkUserLogin(): Boolean = user != null && "" != user?.userId?:""

        //获取用户的登录userid
        val loginUserId: String
            get() = user?.userId?:""

       
    }

    //===================================================================================================================
}
