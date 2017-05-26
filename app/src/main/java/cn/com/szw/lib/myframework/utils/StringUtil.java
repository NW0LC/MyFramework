package cn.com.szw.lib.myframework.utils;

import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String EMAILCHECK = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String PHONECHECK = "^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	private static Pattern mPattern;
	private static Matcher matcher;

	/**
	 * 字符串去处特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

    /**
     * @param index  间距
     * @param strs  字符串
     * @param str  插入字符
     * @return  插入字符后字符串
     */
    public static String insert(int index,String strs,String str) {
		String len="";
        if (strs.length()>index) {
            int oldIndex=0;
            for (int i = index; i < strs.length(); i+=index) {
                len+=strs.substring(oldIndex,i)+str;
                oldIndex = i;
            }

            if ( (oldIndex+index)>=strs.length())
                len+=strs.substring(oldIndex);
        }
		return len;
	}

	/**
	 * 区分字符串是否手机号
	 */
	public static boolean isPhone(String str) {
		mPattern = Pattern.compile(PHONECHECK);
		matcher = mPattern.matcher(str);
		return matcher.matches();

	}
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return format.format(date);

	}
	/**
	 * 当月第一天
	 * @return
	 */
	public static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd",Locale.CHINA);
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();

		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		return df.format(gcLast.getTime());

	}

	/**
	 * 区分字符串是邮箱还是手机号 return 1:手机号 0:email 2:格式不正确
	 */
	public static int isPhoneOrEmail(String str) {
		mPattern = Pattern.compile(EMAILCHECK);
		matcher = mPattern.matcher(str);
		if (matcher.matches()) {
			return 0;
		} else {
			mPattern = Pattern.compile(PHONECHECK);
			matcher = mPattern.matcher(str);
			if (matcher.matches()) {
				return 1;
			} else {
				return 2;
			}
		}
	}

	/**
	 * 字符串提取数字
	 * 
	 * @param str
	 * @return
	 */
	public static int getD(String str) {

		str = str.trim();
		String str2 = "";
		if (!"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return Integer.parseInt(str2);
	}

	/**
	 * 通过秒数获得分钟数
	 */
	public static String secToMinute(int sec) {
		String msg = "";
		if (sec < 60) {
			if (sec < 10) {
				msg = "00:0" + sec;
			} else {
				msg = "00:" + sec;
			}
		} else {
			if (sec / 60 < 10) {
				msg = "0" + sec / 60 + ":";
			} else {
				msg = sec / 60 + ":";
			}
			if (sec % 60 < 10) {
				msg = msg + "0" + sec % 60;
			} else {
				msg = msg + sec % 60;
			}
		}
		return msg;
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * @param e
	 * @return 得到输入字符串
	 */
	public static String getEditTextToString(EditText e){
		return e.getText().toString().trim();
	}
	/**
	 * @param e
	 * @return 得到输入字符串
	 */
	public static String getTextViewToString(TextView e){
		return e.getText().toString().trim();
	}


}
