package com.szw.framelibrary.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import com.common.controls.dialog.DialogBase
import com.common.controls.dialog.DialogUtil

/**
 * 类型104 Dialog
 * 有标题，有自定义布局内容，两个按钮
 */

class DialogType104(context: Context) : DialogBase(context) {
    private var mContentContainer: FrameLayout? = null
    private var mCancelTv: TextView? = null
    private var mOkTv: TextView? = null


    private var mMiddleDivider: View? = null
    private var onBeforeDismiss: OnBeforeDismissListener? = null

    fun setOnBeforeDismiss(onBeforeDismiss: OnBeforeDismissListener) {
        this.onBeforeDismiss = onBeforeDismiss
    }

    override fun dismiss() {
        if (onBeforeDismiss != null)
            if (onBeforeDismiss?.onBeforeDismiss()==true) {
                if (mDialog != null) mDialog.dismiss()
            }
    }

    interface OnBeforeDismissListener {
        fun onBeforeDismiss(): Boolean
    }

    override fun initDialog() {
        val rootView = LayoutInflater.from(mContext).inflate(com.common.alertpop.R.layout.common_dialog_layout_type104, null)
        mTitleTv = rootView.findViewById<View>(com.common.alertpop.R.id.common_dialog_title_text) as TextView
        mContentContainer = rootView.findViewById<View>(com.common.alertpop.R.id.common_dialog_content_container) as FrameLayout
        mCancelTv = rootView.findViewById<View>(com.common.alertpop.R.id.common_dialog_cancel_btn) as TextView
        mOkTv = rootView.findViewById<View>(com.common.alertpop.R.id.common_dialog_ok_btn) as TextView
        mMiddleDivider = rootView.findViewById(com.common.alertpop.R.id.common_dialog_btn_middle_divider)
        createDialog(rootView)
    }

    override fun setContentView(contentLayoutId: Int) {
        LayoutInflater.from(mContext).inflate(contentLayoutId, mContentContainer, true)
    }

    override fun setContentView(contentView: View?) {
        mContentContainer?.addView(contentView)
    }

    override fun setContentView(contentView: View?, params: ViewGroup.LayoutParams?) {
        mContentContainer?.addView(contentView, params)
    }

    override fun setCancelBtn(textId: Int, cancelOnClickListener: View.OnClickListener?) {
        mCancelTv?.setText(textId)
        setOnCancelClickListener(cancelOnClickListener)
    }

    override fun setCancelBtn(text: CharSequence?, cancelOnClickListener: View.OnClickListener?) {
        mCancelTv?.text = text
        setOnCancelClickListener(cancelOnClickListener)
    }

    override fun setOkBtn(textId: Int, okOnClickListener: View.OnClickListener?) {
        mOkTv?.setText(textId)
        setOnOkClickListener(okOnClickListener)
    }

    override fun setOkBtn(text: CharSequence?, okOnClickListener: View.OnClickListener?) {
        mOkTv?.text = text
        setOnOkClickListener(okOnClickListener)
    }

    override fun setOkBtnStyleType(okBtnStyleType: Int) {
        mOkTv?.setBackgroundResource(DialogUtil.getOkBtnBgResId(okBtnStyleType))
        mOkTv?.setTextColor(DialogUtil.getOkBtnTextColorValue(okBtnStyleType, mOkTv?.context))
        mMiddleDivider?.visibility = if (DialogUtil.isBtnMiddleDividerVisible(okBtnStyleType)) View.VISIBLE else View.GONE
    }

    override fun bindAllListeners() {
        mCancelTv?.setOnClickListener { v -> this@DialogType104.onCancelClick(v) }
        mOkTv?.setOnClickListener { v -> this@DialogType104.onOkClick(v) }
    }

}
