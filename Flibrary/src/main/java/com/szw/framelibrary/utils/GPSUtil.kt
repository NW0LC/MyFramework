package com.szw.framelibrary.utils

import com.szw.framelibrary.utils.GPSUtil.gcj02_To_Bd09

object GPSUtil {
    var pi = 3.1415926535897932384626
    var x_pi = 3.14159265358979324 * 3000.0 / 180.0
    var a = 6378245.0
    var ee = 0.00669342162296594323

    fun transformLat(x: Double, y: Double): Double {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
        +0.2 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }

    fun transform(lat: Double, lon: Double): DoubleArray {
        if (outOfChina(lat, lon)) {
            return doubleArrayOf(lat, lon)
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return doubleArrayOf(mgLat, mgLon)
    }

    fun outOfChina(lat: Double, lon: Double): Boolean {
        if (lon < 72.004 || lon > 137.8347)
            return true
        return if (lat < 0.8293 || lat > 55.8271) true else false
    }

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    fun gps84_To_Gcj02(lat: Double, lon: Double): DoubleArray {
        if (outOfChina(lat, lon)) {
            return doubleArrayOf(lat, lon)
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return doubleArrayOf(mgLat, mgLon)
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     */
    fun gcj02_To_Gps84(lat: Double, lon: Double): DoubleArray {
        val gps = transform(lat, lon)
        val lontitude = lon * 2 - gps[1]
        val latitude = lat * 2 - gps[0]
        return doubleArrayOf(latitude, lontitude)
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    fun gcj02_To_Bd09(lat: Double, lon: Double): DoubleArray {
        val z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * x_pi)
        val theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * x_pi)
        val tempLon = z * Math.cos(theta) + 0.0065
        val tempLat = z * Math.sin(theta) + 0.006
        return doubleArrayOf(tempLat, tempLon)
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    fun bd09_To_Gcj02(lat: Double, lon: Double): DoubleArray {
        val x = lon - 0.0065
        val y = lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi)
        val tempLon = z * Math.cos(theta)
        val tempLat = z * Math.sin(theta)
        return doubleArrayOf(tempLat, tempLon)
    }

    /**将gps84转为bd09
     * @param lat
     * @param lon
     * @return
     */
    fun gps84_To_bd09(lat: Double, lon: Double): DoubleArray {
        val gcj02 = gps84_To_Gcj02(lat, lon)
        return gcj02_To_Bd09(gcj02[0], gcj02[1])
    }

    fun bd09_To_gps84(lat: Double, lon: Double): DoubleArray {
        val gcj02 = bd09_To_Gcj02(lat, lon)
        val gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1])
        //保留小数点后六位
        gps84[0] = retain6(gps84[0])
        gps84[1] = retain6(gps84[1])
        return gps84
    }

    /**保留小数点后六位
     * @param num
     * @return
     */
    private fun retain6(num: Double): Double {
        val result = String.format("%.6f", num)
        return java.lang.Double.valueOf(result)!!
    }

}