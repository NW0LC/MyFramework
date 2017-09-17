package com.szw.framelibrary.config

/**
 * Created by ${Swain}
 * on 2016/10/1.
 */
class Constants {
    /**
     * 刷新
     */
    object RefreshState {
        var STATE_REFRESH = 0
        var STATE_LOAD_MORE = 1
    }

    /**
     * 结果回执码
     */
    object Result {
        var Intent_ClassName = "Intent_ClassName"
        var Result_Login_Ok = 200
    }

    /**
     * 网络码
     */
    object NetCode {
        var SUCCESS = 200
    }

    /**
     * 权限请求 requestCode
     */
    object Permission {
        var Location = 100
        var Phone = 200
        var SMS = 300
        var Camera = 400
    }

    /**
     * 回调
     */
    object BusAction {

        /**
         * 支付完成
         */
        val Pay_Finish = "Pay_Finish"

    }

    /**
     * 定位
     */
    object Location {
        val INTENT_ACTION_LOCATION = "intent_action_location"
        val INTENT_DATA_LOCATION_CITY = "intent_data_location_city"
        val INTENT_DATA_LOCATION_LONGITUDE = "intent_data_location_longitude"
        val INTENT_DATA_LOCATION_LATITUDE = "intent_data_location_latitude"
    }
}
