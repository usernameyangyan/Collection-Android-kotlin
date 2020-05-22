package com.youngmanster.collection_kotlin.data

import android.content.Context
import android.content.SharedPreferences
import com.youngmanster.collection_kotlin.config.Config

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class SharePreference private constructor() {

    companion object {
        private var sharePreference: SharePreference? = null
        private var sharedPreferences: SharedPreferences?=null
        fun getInstance(): SharePreference {
            if (sharePreference == null) {
                synchronized(SharePreference::class.java) {
                    if (sharePreference == null) {
                        sharePreference = SharePreference()
                        sharedPreferences=Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
                    }
                }
            }
            return sharePreference!!
        }
    }


    /**
     * 保存String到sp
     * @param key     存的字段名称
     * @param content 内容
     * @return
     */
    fun putString(key: String, content: String?) {
        sharedPreferences!!.edit().putString(key, content).apply()
    }

    /**
     * 保存int到sp
     * @param key     存的字段名称
     * @param content 内容
     * @return
     */
    fun putInt(key: String, content: Int) {
        sharedPreferences!!.edit()
            .putInt(key, content)
            .apply()
    }

    /**
     * 保存Long值
     * @param key
     * @return
     */
    fun putLong(key: String, value: Long) {
        sharedPreferences!!.edit()
            .putLong(key, value)
            .apply()
    }

    /**
     * 保存boolean值
     * @param key
     * @param content
     * @return
     */
    fun putBoolean(key: String, content: Boolean) {
        sharedPreferences!!.edit()
            ?.putBoolean(
                key,
                content
            )?.apply()
    }


    /**
     * 保存Float值
     * @param key
     * @param content
     * @return
     */
    fun putFloat(key: String, content: Float) {
        sharedPreferences!!.edit()
            ?.putFloat(
                key,
                content
            )?.apply()
    }

    /**
     * 获取SP中的String内容
     *
     * @param key
     * @return 失败返回null
     */
    fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences!!.getString(key, defaultValue)
    }


    fun getInt(key: String, defaultValue: Int): Int {
        return  sharedPreferences!!.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return  sharedPreferences!!.getLong(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return  sharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return  sharedPreferences!!.getFloat(key, defaultValue)
    }

}