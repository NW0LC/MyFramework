package cn.com.szw.demo


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import java.util.*

class MainActivity : Activity(), OnRefreshListener {
    lateinit var viewById: SmartRefreshLayout
    val adapter = AddressAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        val rec = findViewById<RecyclerView>(R.id.recyclerView)
        StatusBarUtil.setPaddingSmart(this, rec)
        StatusBarUtil.setPaddingSmart(this, findViewById<View>(R.id.toolbar))
        StatusBarUtil.setPaddingSmart(this, findViewById<View>(R.id.blurview))
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
    }

    override fun onRefresh(refreshlayout: RefreshLayout) {
        viewById.finishRefresh(300)
        startActivity(Intent(this, OtherActivity::class.java))
    }
}
