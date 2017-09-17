package com.szw.framelibrary.utils

import android.widget.EditText
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtil {
    val EMAILCHECK = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
    val PHONECHECK = "^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$"
    private var mPattern: Pattern? = null
    private var matcher: Matcher? = null

    /**
     * 字符串去处特殊字符
     *
     * @param str
     * @return
     */
    fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }

    /**
     * @param index  间距
     * @param strs  字符串
     * @param str  插入字符
     * @return  插入字符后字符串
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
     * 区分字符串是否手机号
     */
    fun isPhone(str: String): Boolean {
        mPattern = Pattern.compile(PHONECHECK)
        matcher = mPattern!!.matcher(str)
        return matcher!!.matches()

    }

    fun getTime(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return format.format(date)

    }

    /**
     * 当月第一天
     * @return
     */
    val firstDay: String
        get() {
            val df = SimpleDateFormat("yyyy.MM.dd", Locale.CHINA)
            val calendar = Calendar.getInstance()
            val theDate = calendar.time

            val gcLast = Calendar.getInstance() as GregorianCalendar
            gcLast.time = theDate
            gcLast.set(Calendar.DAY_OF_MONTH, 1)
            return df.format(gcLast.time)

        }

    /**
     * 区分字符串是邮箱还是手机号 return 1:手机号 0:email 2:格式不正确
     */
    fun isPhoneOrEmail(str: String): Int {
        mPattern = Pattern.compile(EMAILCHECK)
        matcher = mPattern!!.matcher(str)
        if (matcher!!.matches()) {
            return 0
        } else {
            mPattern = Pattern.compile(PHONECHECK)
            matcher = mPattern!!.matcher(str)
            return if (matcher!!.matches()) {
                1
            } else {
                2
            }
        }
    }

    /**
     * 字符串提取数字
     *
     * @param str
     * @return
     */
    fun getD(str: String): Int {
        var str = str

        str = str.trim { it <= ' ' }
        var str2 = ""
        if ("" != str) {
            for (i in 0..str.length - 1) {
                if (str[i].toInt() >= 48 && str[i].toInt() <= 57) {
                    str2 += str[i]
                }
            }
        }
        return Integer.parseInt(str2)
    }

    /**
     * 通过秒数获得分钟数
     */
    fun secToMinute(sec: Int): String {
        var msg = ""
        if (sec < 60) {
            if (sec < 10) {
                msg = "00:0" + sec
            } else {
                msg = "00:" + sec
            }
        } else {
            if (sec / 60 < 10) {
                msg = "0" + sec / 60 + ":"
            } else {
                msg = (sec / 60).toString() + ":"
            }
            if (sec % 60 < 10) {
                msg = msg + "0" + sec % 60
            } else {
                msg = msg + sec % 60
            }
        }
        return msg
    }

    fun isBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.length == 0
    }

    fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
    }

    /**
     * @param e
     * @return 得到输入字符串
     */
    fun getEditTextToString(e: EditText): String {
        return e.text.toString().trim { it <= ' ' }
    }

    /**
     * @param e
     * @return 得到输入字符串
     */
    fun getTextViewToString(e: TextView): String {
        return e.text.toString().trim { it <= ' ' }
    }


}
