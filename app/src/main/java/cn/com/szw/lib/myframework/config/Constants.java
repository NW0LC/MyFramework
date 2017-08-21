package cn.com.szw.lib.myframework.config;

/**
 * Created by ${Swain}
 * on 2016/10/1.
 */
public class Constants {
	/**
	 * 刷新
	 */
	public static class RefreshState{
		public static int STATE_REFRESH=0;
		public static int STATE_LOAD_MORE =1;
	}
	/**
	 * 结果回执码
	 */
	public static class Result{
		public static String Intent_ClassName ="Intent_ClassName";
		public static int Result_Login_Ok =200;
	}
	/**
	 * 网络码
	 */
	public static class NetCode{
		public static int SUCCESS=200;
	}
	/**
	 * 权限请求 requestCode
	 */
	public static class Permission{
		public static int Location=100;
		public static int Phone=200;
		public static int Camera=300;
	}
	/**
	 * 回调
	 */
	public static class BusAction{

		/**
		 * 支付完成
		 */
		public static final String Pay_Finish ="Pay_Finish";

	}

	/**
	 * 定位
	 */
	public static class Location{
		public static final String INTENT_ACTION_LOCATION="intent_action_location";
		public static final String INTENT_DATA_LOCATION_CITY="intent_data_location_city";
		public static final String INTENT_DATA_LOCATION_LONGITUDE="intent_data_location_longitude";
		public static final String INTENT_DATA_LOCATION_LATITUDE="intent_data_location_latitude";
	}
}
