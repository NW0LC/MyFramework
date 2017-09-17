package com.szw.framelibrary.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup

import com.hwangjr.rxbus.RxBus
import com.szw.framelibrary.view.CustomProgress


 abstract class MyBaseFragment : Fragment() {
    lateinit var rootView:View
    override fun onDestroy() {
        try {
            RxBus.get().unregister(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        CustomProgress.disMiss()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val view = rootView.parent as ViewGroup
        view.removeView(rootView)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        try {
            RxBus.get().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    abstract fun initView()
    fun initEvent() {}
}
