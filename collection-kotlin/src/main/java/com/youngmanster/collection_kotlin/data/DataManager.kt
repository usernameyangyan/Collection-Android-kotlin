package com.youngmanster.collection_kotlin.data

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import com.youngmanster.collection_kotlin.data.database.PagingList
import com.youngmanster.collection_kotlin.data.database.ResultSet
import com.youngmanster.collection_kotlin.data.database.SQLiteDataBase
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.RequestManager
import com.youngmanster.collection_kotlin.utils.LogUtils
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class DataManager{

    class DataForSqlite{
        companion object{
            /**
             * 插入数据
             * -1 代表失败
             */
            fun <T> insert(model: T): Boolean? {
                return SQLiteDataBase.getInstance().insert(model)
            }


            /**
             * 批量插入数据
             */
            fun <T> insertList(clazz: Class<T>, dataList: List<T>): Boolean {
                return SQLiteDataBase.getInstance().batchInsert(clazz,dataList)
            }

            fun <T> insertListBySync(clazz: Class<T>, dataList: List<T>,onInsertDataCompleteListener: SQLiteDataBase.InsertDataCompleteListener) {
                SQLiteDataBase.getInstance().batchInsertBySync(clazz,dataList,onInsertDataCompleteListener)
            }


            /**
             * 根据条件查询
             * selection:条件语句 ：id=?   id=? or age=?
             */
            fun <T> queryByFirstByWhere(
                clazz: Class<T>,
                selection: String,
                vararg selectionArgs: String
            ): T? {
                return SQLiteDataBase.getInstance().queryByFirstByWhere(clazz,selection,
                    *selectionArgs)
            }

            /**
             * 查询表里的全部数据
             */
            fun <T> queryAll(clazz: Class<T>): List<T>? {
                return SQLiteDataBase.getInstance().queryAll(clazz)
            }

            /**
            * 根据条件查询表里的全部数据
            */
            fun <T> queryAllByWhere(clazz: Class<T>,selection: String,
                                    vararg selectionArgs: String): List<T>? {
                return SQLiteDataBase.getInstance().queryAllByWhere(clazz,selection,*selectionArgs)
            }

            fun <T> queryAllBySync(clazz: Class<T>,onQueryDataComplete: SQLiteDataBase.QueryDataCompleteListener<T>){
                SQLiteDataBase.getInstance().queryAllBySync(clazz,onQueryDataComplete)
            }

            /**
             * 查询表里的第一条数据
             */
            fun <T> queryByFirst(clazz: Class<T>): T? {
                return SQLiteDataBase.getInstance().queryByFirst(clazz)
            }


            /**
             * 根据条件删除
             * selection:条件语句 ：id=?   id=? or age=?
             */

            fun <T> delete(clazz: Class<T>, whereClause: String, vararg whereArgs: String?): Boolean {
                return SQLiteDataBase.getInstance().delete(clazz,whereClause, *whereArgs)
            }
            /**
             * 删除全部数据
             */
            fun <T> deleteAll(clazz: Class<T>): Boolean {
                return SQLiteDataBase.getInstance().deleteAll(clazz)
            }

            /**
             * 删除表
             */
            fun <T> deleteTable(clazz: Class<T>) {
                 SQLiteDataBase.getInstance().deleteTable(clazz)
            }



            /**
             * 更新数据
             * -1失败
             */
            fun <T> update(model: T, whereClause: String, vararg whereArgs: String): Boolean? {
                return SQLiteDataBase.getInstance().update(model,whereClause, *whereArgs)
            }


            /***
             * 分页查询，实体类必须包含PrimaryKey
             */


            fun <T> queryOfPageByWhere(
                clazz: Class<T>,
                selection: String?,
                selectionArgs: Array<String>?,
                page: Int,
                pageSize: Int
            ): PagingList<T>? {

                return SQLiteDataBase.getInstance().queryOfPageByWhere(clazz,selection,selectionArgs,page,pageSize)

            }

            fun <T> queryOfPage(
                clazz: Class<T>,
                page: Int,
                pageSize: Int
            ): PagingList<T>? {
                return SQLiteDataBase.getInstance().queryOfPage(clazz,page,pageSize)
            }

            /**
             * 使用SQL语句
             */

            fun execQuerySQL(sql: String): List<ResultSet>? {
                return SQLiteDataBase.getInstance().execQuerySQL(sql)
            }


            /**
             * 更新表,用于更新表格字段，只可增加字段，需要配合版本号已经SQLiteVersionMigrate使用
             */
            fun <T> updateTable(clazz: Class<T>) {
                SQLiteDataBase.getInstance().updateTable(clazz)
            }

        }

    }

    class DataForHttp{
        companion object{
            fun <T> httpRequest(requestBuilder: RequestBuilder<T>): DisposableObserver<ResponseBody>? {
                return RequestManager.getInstance().request(requestBuilder)
            }
        }
    }

    class DataForSharePreferences{
        companion object{
            fun <T> saveObject(key:String,con:T){

                when (con) {
                    is Int -> {
                        SharePreference.getInstance().putInt(key,con)
                    }
                    is String -> {
                        SharePreference.getInstance().putString(key,con)
                    }
                    is Boolean -> {
                        SharePreference.getInstance().putBoolean(key,con)
                    }
                    is Long -> {
                        SharePreference.getInstance().putLong(key,con)
                    }
                    is Float -> {
                        SharePreference.getInstance().putFloat(key,con)
                    }
                    else -> LogUtils.error("DataManager","暂不支持该类型数据")
                }
            }

            @Suppress("UNCHECKED_CAST")
            fun <T>getObject(key:String,defaultValue:T):T? {
                when (defaultValue) {
                    is Int -> {
                        return SharePreference.getInstance().getInt(key,defaultValue) as T
                    }
                    is String -> {
                        return SharePreference.getInstance().getString(key,defaultValue) as? T
                    }
                    is Boolean -> {
                        return SharePreference.getInstance().getBoolean(key,defaultValue) as? T
                    }
                    is Long -> {
                        return SharePreference.getInstance().getLong(key,defaultValue) as? T
                    }
                    is Float -> {
                        return SharePreference.getInstance().getFloat(key,defaultValue) as? T
                    }
                }

                return null
            }

        }
    }


    class DataForMMKV{
        companion object{
            fun <T> saveObject(key:String,con:T){
                when (con) {
                    is Int -> {
                        MMKVWithLocalData.getInstance().encodeInt(key,con)
                    }
                    is String -> {
                        MMKVWithLocalData.getInstance().encodeString(key,con)
                    }
                    is Boolean -> {
                        MMKVWithLocalData.getInstance().encodeBoolean(key,con)
                    }
                    is Long -> {
                        MMKVWithLocalData.getInstance().encodeLong(key,con)
                    }
                    is Float -> {
                        MMKVWithLocalData.getInstance().encodeFloat(key,con)
                    }

                    is Double -> {
                        MMKVWithLocalData.getInstance().encodeDouble(key,con)
                    }

                    is ByteArray -> {
                        MMKVWithLocalData.getInstance().encodeByteArray(key,con)
                    }

                    is Parcelable -> {
                        MMKVWithLocalData.getInstance().encodeParcelable(key,con)
                    }

                    else -> LogUtils.error("DataManager","暂不支持该类型数据")
                }
            }

            @Suppress("UNCHECKED_CAST")
            fun <T>getObject(key:String,defaultValue:T):T? {
                when (defaultValue) {
                    is Int -> {
                        return MMKVWithLocalData.getInstance().decodeInt(key,defaultValue) as T
                    }
                    is String -> {
                        return MMKVWithLocalData.getInstance().decodeString(key,defaultValue) as? T
                    }
                    is Boolean -> {
                        return MMKVWithLocalData.getInstance().decodeBool(key,defaultValue) as? T
                    }
                    is Long -> {
                        return MMKVWithLocalData.getInstance().decodeLong(key,defaultValue) as? T
                    }
                    is Float -> {
                        return MMKVWithLocalData.getInstance().decodeFloat(key,defaultValue) as? T
                    }

                    is Double -> {
                        return MMKVWithLocalData.getInstance().decodeDouble(key,defaultValue) as? T
                    }

                    is ByteArray -> {
                        return MMKVWithLocalData.getInstance().decodeByteArray(key,defaultValue) as? T
                    }

                    is Parcelable -> {
                        return MMKVWithLocalData.getInstance().decodeParcelable(key,defaultValue.javaClass) as? T
                    }
                }

                return null
            }

            fun removeKey(key: String){
                MMKVWithLocalData.getInstance().removeKey(key)
            }

            fun removeKeys(keys: Array<String>) {
                MMKVWithLocalData.getInstance().removeKeys(keys)
            }

            fun getAllKeys(): Array<out String>? {
                return MMKVWithLocalData.getInstance().getAllKeys()
            }

            fun hasKey(key: String): Boolean? {
                return MMKVWithLocalData.getInstance().hasKey(key)
            }

            fun have(key: String): Boolean? {
                return MMKVWithLocalData.getInstance().have(key)
            }

            fun clearAll() {
                MMKVWithLocalData.getInstance().clearAll()
            }

            fun getMkv(): MMKV? {
                return MMKVWithLocalData.getInstance().getMkv()
            }

        }
    }
}