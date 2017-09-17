package com.szw.framelibrary.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.common.controls.dialog.CommonDialogFactory
import com.common.controls.dialog.DialogUtil
import com.common.controls.dialog.ICommonDialog
import com.szw.framelibrary.base.BaseActivity
import java.util.regex.Pattern


/**
 * Created by Swain
 * on 2016/11/18.
 */

object DialogUtils {
    var dialog: ICommonDialog ?= null

    fun Call(context: BaseActivity, str: String) {
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(context, "暂无电话", Toast.LENGTH_SHORT).show()
            return
        }
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        val str1 = m.replaceAll("").trim { it <= ' ' }

        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_1)
        dialog?.setTitleText(if (TextUtils.isEmpty(str1)) "暂无电话" else str1)
        dialog?.setCancelBtn("取消") { dialog?.dismiss() }
        dialog?.setOkBtn("拨打", View.OnClickListener {
            if (TextUtils.isEmpty(str1)) {
                Toast.makeText(context, "暂无可拨打号码", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:" + str1))
            context.PermissionCallPhoneWithCheck(intent, false)
            dialog?.dismiss()
        })
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()
    }

    fun reminder(context: Context) {
        reminder(context, "", "", null)
    }

    fun reminder(context: Context, title: String) {
        reminder(context, title, "", null)
    }

    fun reminder(context: Context, title: String, content: String) {
        reminder(context, title, content, null)
    }

    fun reminder(context: Context, title: String, content: String, ok: String, cancel: String) {
        reminder(context, title, content, null, ok, cancel)
    }

    fun reminder(context: Context, title: String, content: String, ok: String, cancel: String, listener: View.OnClickListener) {
        reminder(context, title, content, listener, ok, cancel)
    }

    fun reminder(context: Context, title: String, content: String, listener: View.OnClickListener?, vararg btnStr: String) {
        var cancel = "取消"
        var ok = "确定"
        if (btnStr != null) {
            if (btnStr.size >= 1)
                ok = btnStr[0]
            if (btnStr.size >= 2)
                cancel = btnStr[1]
        }
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog?.setTitleText(if (TextUtils.isEmpty(title)) "提示" else title)
        dialog?.setContentText(if (TextUtils.isEmpty(content)) "确定要这样做吗？" else content)
        dialog?.setCancelBtn(cancel) { dialog?.dismiss() }
        dialog?.setOkBtn(ok) { v ->
            dialog?.dismiss()
            listener?.onClick(v)
        }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()
    }

    fun Warning(context: Context, str: String) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_4)
        dialog?.setTitleText(if (TextUtils.isEmpty(str)) "空空如也" else str)
        dialog?.setOkBtn("确定") { dialog?.dismiss() }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()
    }

    fun WarningWithListener(context: Context, str: String, listener: View.OnClickListener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_4)
        dialog?.setTitleText(if (TextUtils.isEmpty(str)) "空空如也" else str)
        dialog?.setOkBtn("确定") { v ->
            dialog?.dismiss()
            listener.onClick(v)
        }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()
    }


}
