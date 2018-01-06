package cn.com.szw.demo


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.AppUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.umeng.message.inapp.InAppMessageManager
import ezy.boost.update.UpdateInfo
import ezy.boost.update.UpdateManager
import ezy.boost.update.UpdateUtil
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject


class MainActivity : BaseActivity(), OnRefreshListener {
    override fun initToolbar(): Boolean {
        return false
    }

    override fun setInflateId(): Int =R.layout.activity_main

    lateinit var viewById: SmartRefreshLayout
    val adapter = AddressAdapter()

    override fun init() {
        InAppMessageManager.getInstance(this).showCardMessage(this, "main"
        ) {
                toast("走了走了")
        }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        val rec = findViewById<RecyclerView>(R.id.recyclerView)
        StatusBarUtil.setPaddingSmart(this, rec)
        StatusBarUtil.setPaddingSmart(this, findViewById<View>(R.id.toolbar))
        StatusBarUtil.setPaddingSmart(this, findViewById<View>(R.id.blurView))
        StatusBarUtil.setMargin(this, findViewById<View>(R.id.gifview))

        rec.layoutManager = LinearLayoutManager(this)

        rec.adapter = adapter
        val data = ArrayList<String>()
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        data.add("")
        adapter.setNewData(data)
        val params = HashMap<String, String>()
        OkGo.post<NetEntity<List<String>>>("http://www.fengwalker.com/App/BeeFrog/Main/MainTop.aspx")
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<String>>>(this) {
                    override fun onSuccess(response: Response<NetEntity<List<String>>>) {
                        Toast.makeText(this@MainActivity, "response.body():" + response.body(), Toast.LENGTH_SHORT).show()
                        adapter.setNewData(response.body().info)
                        val list = response.body().data
                        if (list != null) {
                            adapter.addData(list)
                        }
                    }
                })

        viewById = findViewById(R.id.refreshLayout)
        viewById.setOnRefreshListener(this)
        UpdateUtil.clean(this)
        check(true, "http://www.cngjtx.com/App/Upgrade.aspx",998)

    }

    /**
     * [isManual] 是否是手动更新   true 是  false 否
     */
    private fun check(isManual: Boolean,checkUrl:String, notifyId: Int) {
        UpdateManager.create(this).setUrl(checkUrl)
                .setPostData("device=1&versionCode=${AppUtils.getAppVersionCode()}")
                .setManual(isManual).setNotifyId(notifyId)
                .setParser { parse(it) }.check()
    }

    @Throws(JSONException::class)
    private fun parse(s: String): UpdateInfo {
        val o = JSONObject(s)
        return parse(if (o.has("info")) o.getJSONObject("info") else o)
    }

    private fun parse(o: JSONObject?): UpdateInfo {
        val info = UpdateInfo()
        if (o == null) {
            return info
        }
        info.hasUpdate = o.optBoolean("hasUpdate", false)
        if (!info.hasUpdate) {
            return info
        }
        info.isSilent = o.optBoolean("isSilent", false)
        info.isForce = o.optBoolean("isForce", false)
        info.isAutoInstall = o.optBoolean("isAutoInstall", !info.isSilent)
        info.isIgnorable = o.optBoolean("isIgnorable", true)

        info.versionCode = o.optInt("android_VersionCode", 0)
        info.versionName = o.optString("android_VersionName")
        info.updateContent = o.optString("updateContent")

        info.url = o.optString("url")
        info.md5 = o.optString("md5")
        info.size = o.optLong("size", 0)

        return info
    }
    override fun onRefresh(refreshlayout: RefreshLayout) {
        viewById.finishRefresh(300)
//        startActivity(Intent(this, OtherActivity::class.java))
        DialogUtils.Call(this,"13810711669")
//        val intent = Intent(mContext, PreviewActivity::class.java)
//        val imgs = ArrayList<String>()
//        imgs.add("http://img5.imgtn.bdimg.com/it/u=603621955,1179636658&fm=27&gp=0.jpg")
//        imgs.add("http://img1.imgtn.bdimg.com/it/u=829730016,3409799239&fm=27&gp=0.jpg")
//        imgs.add("http://img0.imgtn.bdimg.com/it/u=2117879835,2827064527&fm=27&gp=0.jpg")
//        imgs.add("http://img0.imgtn.bdimg.com/it/u=2117879835,2827064527&fm=27&gp=0.jpg")
//        imgs.add("http://img0.imgtn.bdimg.com/it/u=2117879835,2827064527&fm=27&gp=0.jpg")
//        intent.putExtra(PREVIEW_INTENT_IMAGES, imgs)
//        intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
//        intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, true)
//        startActivity(intent)
    }
}
