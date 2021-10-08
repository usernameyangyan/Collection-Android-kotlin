package com.youngmanster.collection_kotlin.data

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import com.youngmanster.collection_kotlin.config.Config

/**
 * Created by yangy
 *2021/3/25
 *Describe:
 */
class MMKVWithLocalData private constructor() {
    companion object {
        private var mMKVWithLocalData: MMKVWithLocalData? = null
        private var mkv: MMKV? = null
        fun getInstance(): MMKVWithLocalData {
            if (mMKVWithLocalData == null) {
                synchronized(MMKVWithLocalData::class.java) {
                    if (mMKVWithLocalData == null) {
                        mMKVWithLocalData = MMKVWithLocalData()
                        MMKV.initialize(Config.CONTEXT!!)
                        mkv = MMKV.defaultMMKV()
                    }
                }
            }
            return mMKVWithLocalData!!
        }
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */


    fun encodeInt(key: String, value: Int) {
        mkv?.encode(key, value)
    }

    fun encodeString(key: String, value: String) {
        mkv?.encode(key, value)
    }

    fun encodeBoolean(key: String, value: Boolean) {
        mkv?.encode(key, value)
    }

    fun encodeFloat(key: String, value: Float) {
        mkv?.encode(key, value)
    }

    fun encodeLong(key: String, value: Long) {
        mkv?.encode(key, value)
    }

    fun encodeDouble(key: String, value: Double) {
        mkv?.encode(key, value)
    }

    fun encodeByteArray(key: String, value: ByteArray) {
        mkv?.encode(key, value)
    }


    fun encodeParcelable(key: String, value: Parcelable) {
        mkv?.encode(key, value)
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */

    fun decodeInt(key: String, defaultValue: Int): Int {
        return mkv?.decodeInt(key, defaultValue)!!
    }

    fun decodeDouble(key: String, defaultValue: Double): Double {
        return mkv?.decodeDouble(key, defaultValue)!!
    }

    fun decodeLong(key: String, defaultValue: Long): Long {
        return mkv?.decodeLong(key, defaultValue)!!
    }

    fun decodeBool(key: String, defaultValue: Boolean): Boolean {
        return mkv?.decodeBool(key, defaultValue)!!
    }

    fun decodeFloat(key: String, defaultValue: Float): Float {
        return mkv?.decodeFloat(key, defaultValue)!!
    }

    fun decodeByteArray(key: String, defaultValue: ByteArray): ByteArray {
        return mkv?.decodeBytes(key, defaultValue)!!
    }

    fun decodeString(key: String, defaultValue: String): String {
        return mkv?.decodeString(key, defaultValue)!!
    }


    fun decodeParcelable(key: String, defaultValue: Class<Parcelable>): Parcelable {
        return mkv?.decodeParcelable(key, defaultValue)!!
    }

    /**
     * 移除某个key对
     *
     * @param key
     */

    fun removeKey(key: String) {
        mkv?.removeValueForKey(key)
    }

    /**
     * 移除多个key对
     *
     * @param key
     */

    fun removeKeys(keys: Array<String>) {
        mkv?.removeValuesForKeys(keys)
    }

    /**
     * 获取全部key对
     */

    fun getAllKeys(): Array<out String>? {
        return mkv?.allKeys()
    }

    /**
     * 含有某个key
     *
     * @param key
     * @return
     */
    fun hasKey(key: String): Boolean? {
        return mkv?.containsKey(key)
    }

    /**
     * 含有某个key
     *
     * @param key
     * @return
     */
    fun have(key: String): Boolean? {
        return mkv?.contains(key)
    }

    /**
     * 清除所有key
     */

    fun clearAll() {
        mkv?.clearAll()
    }

    /**
     * 获取操作对象
     *
     * @return
     */

    fun getMkv(): MMKV? {
        return mkv
    }
}