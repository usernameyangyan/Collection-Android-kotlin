package com.youngmanster.collection_kotlin.config

import android.annotation.SuppressLint
import android.content.Context

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class Config{
    companion object{
        /**必传参数 */
        //是否为BuildConfig.DEBUG,日志输出需要
        var DEBUG: Boolean = true
        //设置Context
        @SuppressLint("StaticFieldLeak")
        var CONTEXT: Context? = null
        /**Retrofit */
        //网络请求的域名
        var URL_DOMAIN: String? = null
        //网络缓存地址
        var URL_CACHE: String? = null
        //设置OkHttp的缓存机制的最大缓存时间,默认为一天
        var MAX_CACHE_SECONDS = (60 * 60 * 24).toLong()
        //缓存最大的内存,默认为10M
        var MAX_MEMORY_SIZE = (10 * 1024 * 1024).toLong()
        //设置网络请求json通用解析类
        var MClASS: Class<*>? = null
        var EXPOSEPARAM: String? = null
        /**SharePreference */
        var USER_CONFIG: String = "collection_kotlin_library_user_config"

        /***请求接口超时设定 */
        var CONNECT_TIMEOUT_SECONDS = 60
        var READ_TIMEOUT_SECONDS = 60
        var WRITE_TIMEOUT_SECONDS = 60

        /***设置全局请求头 */
        var HEADERS: Map<String, String>? = null

        /*****数据库设置**/
        var SQLITE_DB_NAME="collection_kotlin_library.db"
        var SQLITE_DB_VERSION=0
    }
}