package com.youngmanster.collection_kotlin.data

import android.content.Context
import com.youngmanster.collection_kotlin.config.Config

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class SharePreference private constructor() {

    companion object {
        private var sharePreference: SharePreference? = null
        fun getInstance(): SharePreference {
            if (sharePreference == null) {
                synchronized(SharePreference::class.java) {
                    if (sharePreference == null) {
                        sharePreference = SharePreference()
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
        Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .edit()
            .putString(key, content)
            .apply()
    }

    /**
     * 保存int到sp
     * @param key     存的字段名称
     * @param content 内容
     * @return
     */
    fun putInt(key: String, content: Int) {
        Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE).edit()
            .putInt(key, content)
            .apply()
    }

    /**
     * 保存Long值
     * @param key
     * @return
     */
    fun putLong(key: String, value: Long) {
        Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE).edit()
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
        Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)?.edit()
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
        Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)?.edit()
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
        return Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .getString(key, defaultValue)
    }


    fun getInt(key: String, defaultValue: Int): Int {
        return Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .getLong(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .getBoolean(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return Config.CONTEXT!!.getSharedPreferences(Config.USER_CONFIG, Context.MODE_PRIVATE)
            .getFloat(key, defaultValue)
    }

}