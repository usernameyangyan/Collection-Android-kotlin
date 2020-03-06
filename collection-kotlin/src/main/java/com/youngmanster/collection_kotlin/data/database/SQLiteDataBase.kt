package com.youngmanster.collection_kotlin.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.network.rx.utils.RxAsyncTask
import com.youngmanster.collection_kotlin.network.rx.utils.RxJavaUtils
import com.youngmanster.collection_kotlin.utils.LogUtils
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */

class SQLiteDataBase {

    interface QueryDataCompleteListener<T>{
        fun onQueryComplete(datas:List<T>?)
    }

    interface InsertDataCompleteListener{
        fun onInsertDataComplete(isInsert:Boolean?)
    }

    private constructor()

    companion object {
        private var db: DbSQLite? = null
        private var sqliteDataBase: SQLiteDataBase? = null
        fun getInstance(): SQLiteDataBase {
            if (sqliteDataBase == null) {
                synchronized(SQLiteDataBase().javaClass) {
                    if (sqliteDataBase == null) {
                        sqliteDataBase = SQLiteDataBase()
                        db = DbSQLite(
                            Config.CONTEXT!!,
                            Config.CONTEXT!!.openOrCreateDatabase(
                                Config.SQLITE_DB_NAME,
                                Context.MODE_PRIVATE,
                                null
                            )
                        )
                    }
                }
            }
            return sqliteDataBase!!
        }
    }


    /**
     * 插入数据
     * -1 代表失败
     */
    fun <T> insert(model: T): Boolean? {
        db?.execSQL(SqlHelper.createTable((model as Any).javaClass))
        val contentValues = ContentValues()
        SqlHelper.parseModelToContentValues(model, contentValues)
        return db!!.insertOrReplace(
            SqlHelper.getBeanName((model as Any).javaClass.name),
            contentValues
        )!! > -1
    }


    /**
     * 批量插入数据
     */
    fun <T> batchInsert(clazz: Class<T>, dataList: List<T>): Boolean {
        db?.execSQL(SqlHelper.createTable(clazz))
        val listVal = ArrayList<ContentValues>()
        for (model in dataList) {
            val contentValues = ContentValues()
            SqlHelper.parseModelToContentValues(model, contentValues)
            listVal.add(contentValues)
        }

        return db?.batchInsert(SqlHelper.getBeanName(clazz.name), listVal)!!
    }

    fun <T> batchInsertBySync(clazz: Class<T>, dataList: List<T>,onInsertDataCompleteListener:InsertDataCompleteListener) {

        RxJavaUtils.executeAsyncTask(object :RxAsyncTask<String,Boolean>(""){
            override fun doInUIThread(t: Boolean?) {
                onInsertDataCompleteListener.onInsertDataComplete(t)
            }

            override fun doInIOThread(t: String?): Boolean {
                db?.execSQL(SqlHelper.createTable(clazz))
                val listVal = ArrayList<ContentValues>()
                for (model in dataList) {
                    val contentValues = ContentValues()
                    SqlHelper.parseModelToContentValues(model, contentValues)
                    listVal.add(contentValues)
                }
                return db?.batchInsert(SqlHelper.getBeanName(clazz.name), listVal)!!
            }

        })


    }



    /**
     * 查找满足条件的第一条数据
     */

    private fun <T> query(
        clazz: Class<T>,
        columns: Array<String>?, selection: String?, selectionArgs: Array<String>?,
        groupBy: String?, having: String?, orderBy: String?
    ): List<T>? {
        try {
            val queryList =
                db?.query(
                    SqlHelper.getBeanName(className = clazz.name),
                    columns,
                    selection,
                    selectionArgs,
                    groupBy,
                    having,
                    orderBy
                )
            if (queryList == null || queryList.isEmpty()) {
                return null
            }
            val resultList = ArrayList<T>()
            SqlHelper.parseResultSetListToModelList(queryList, resultList, clazz)
            return resultList

        }catch (e: SQLiteException){

        }

        return null
    }

    /**
     * 根据条件查询
     * selection:条件语句 ：id=?   id=? or age=?
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> queryByFirstByWhere(
        clazz: Class<T>,
        selection: String,
        vararg selectionArgs: String
    ): T? {
        val resultList =
            query(clazz, null, selection, selectionArgs as Array<String>, null, null, null)
        return if (resultList != null && resultList.isNotEmpty()) {
            resultList[0]
        } else {
            null
        }
    }

    /**
     * 查询表里的全部数据
     */
    fun <T> queryAll(clazz: Class<T>): List<T>? {
        return query(clazz, null, null, null, null, null, null)
    }

    /**
     * 根据条件查询表里的全部数据
     */
    fun <T> queryAllByWhere(clazz: Class<T>,selection: String,
                            vararg selectionArgs: String): List<T>? {
        return query(clazz, null, selection, selectionArgs as Array<String>, null, null, null)
    }

    fun <T> queryAllBySync(clazz: Class<T>,onQueryDataComplete:QueryDataCompleteListener<T>){

        RxJavaUtils.executeAsyncTask(object : RxAsyncTask<String, List<T>>(""){
            override fun doInUIThread(t: List<T>?) {
                onQueryDataComplete.onQueryComplete(t)
            }

            override fun doInIOThread(t: String?): List<T>? {
                return queryAll(clazz)
            }

        })
    }

    /**
     * 查询表里的第一条数据
     */
    fun <T> queryByFirst(clazz: Class<T>): T? {
        val resultList = queryAll(clazz)
        return if (resultList != null && resultList.isNotEmpty()) {
            resultList[0]
        } else {
            null
        }
    }


    /**
     * 根据条件删除
     * selection:条件语句 ：id=?   id=? or age=?
     */

    fun <T> delete(clazz: Class<T>, whereClause: String, vararg whereArgs: String?): Boolean {
        try {
            return db?.delete(SqlHelper.getBeanName(clazz.name), whereClause, whereArgs)!! > 0
        }catch (e:SQLiteException){

        }

        return false

    }

    /**
     * 删除全部数据
     */
    fun <T> deleteAll(clazz: Class<T>): Boolean {
        return delete(clazz, "1")
    }


    /**
     * 删除表
     */
    fun <T> deleteTable(clazz: Class<T>) {
        try {
            val dropTableSql = String.format("DROP TABLE %s", SqlHelper.getBeanName(clazz.name))
            db?.execSQL(dropTableSql)
        }catch (e:SQLiteException){

        }

    }

    /**
     * 更新数据
     * -1失败
     */
    fun <T> update(model: T, whereClause: String, vararg whereArgs: String): Boolean? {
        try {
            val contentValues = ContentValues()
            SqlHelper.parseModelToContentValues(model, contentValues)
            return db?.update(
                SqlHelper.getBeanName((model as Any).javaClass.name),
                contentValues,
                whereClause,
                whereArgs
            ) == 1
        }catch (e:SQLiteException){

        }

        return false

    }

    /**
     *  分页查询
     */
    private fun <T> pagingQuery(
        clazz: Class<T>,
        columns: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, groupBy: String?, having: String?,
        orderBy: String?, page: Int, pageSize: Int
    ): PagingList<T>? {

        try {
            var order = orderBy

            if (orderBy == null) {
                order = SqlHelper.getPrimaryKey(clazz)
            }

            val queryList = db?.pagingQuery(
                SqlHelper.getBeanName(clazz.name), columns, selection, selectionArgs,
                groupBy, having, order, page, pageSize
            ) ?: return null

            val resultList = PagingList<T>()
            resultList.setTotalSize(queryList.getTotalSize())
            SqlHelper.parseResultSetListToModelList(queryList, resultList, clazz)
            return resultList
        }catch (e:SQLiteException){

        }

       return null
    }

    fun <T> queryOfPageByWhere(
        clazz: Class<T>,
        selection: String?,
        selectionArgs: Array<String>?,
        page: Int,
        pageSize: Int
    ): PagingList<T>? {
        return pagingQuery(clazz, null, selection, selectionArgs, null, null, null, page, pageSize)
    }

    fun <T> queryOfPage(
        clazz: Class<T>,
        page: Int,
        pageSize: Int
    ): PagingList<T>? {
        return pagingQuery(clazz, null, null, null, null, null, null, page, pageSize)
    }

    /**
     * 使用SQL语句
     */

    fun execQuerySQL(sql: String): List<ResultSet>? {
        try {
            return db?.execQuerySQL(sql)
        }catch (e:SQLiteException){

        }

        return null

    }


    /**
     * 更新表
     */
    fun <T> updateTable(clazz: Class<T>) {

        try {
            val newTableVersion = SqlHelper.getTableVersion()
            val curTableVersion = getCurTableVersion()
            if (newTableVersion != curTableVersion) {
                DBTransaction.transact(db!!, object : DBTransaction.DBTransactionInterface {
                    override fun onTransact() {
                        val rs = db?.query(
                            "sqlite_master",
                            arrayOf("sql"),
                            "type=? AND name=?",
                            arrayOf("table", SqlHelper.getBeanName(className = clazz.name))
                        )
                        val curTableSql = rs?.get(0)?.getStringValue("sql")

                        val newColumnInfos = getTableColumnInfos(clazz)
                        var curColumns = getTableColumnsInfo(curTableSql!!).toMutableMap()
                        val newColumnSize = newColumnInfos.size
                        var newColumnInfo: ColumnInfo
                        var newColumnName: String
                        var sql: String
                        for (index in 0 until newColumnSize) {
                            newColumnInfo = newColumnInfos[index]
                            newColumnName = newColumnInfo.name.toLowerCase()

                            if (curColumns.containsKey(newColumnName)) {
                                curColumns[newColumnName] = false
                            } else {

                                sql = SqlHelper.getAddColumnSql(
                                    SqlHelper.getBeanName(clazz.name),
                                    newColumnInfo
                                )
                                db?.execSQL(sql)
                            }
                        }


                        DataManager.DataForSharePreferences.saveObject(
                            SqlHelper.PREFS_TABLE_VERSION_KEY,
                            Config.SQLITE_DB_VERSION
                        )
                    }
                })
            }
        }catch (e:SQLiteException){

        }


    }


    /**
     * get current table version
     * @return
     */
    private fun  getCurTableVersion(): Int? {
        return DataManager.DataForSharePreferences.getObject(
            SqlHelper.PREFS_TABLE_VERSION_KEY,0
        )
    }

    /**
     * get table columns in createSql
     * @param createSql
     * @return map, key is column name, value default true means need to delete
     */
    private fun getTableColumnsInfo(createSql: String): Map<String, Boolean> {
        val subSql = createSql.substring(createSql.indexOf('(') + 1, createSql.lastIndexOf(')'))
        val columnInfos = subSql.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val tableInfo = HashMap<String, Boolean>()

        var columnName: String
        var columnInfo: String
        for (i in 0 until columnInfos.size) {
            columnInfo = columnInfos[i].trim { it <= ' ' }
            columnName = columnInfo.substring(0, columnInfo.indexOf(' '))
            tableInfo[columnName.toLowerCase()] = true
        }

        return tableInfo
    }


    /**
     * return info about table's all columns
     * @param clazz
     * @return
     */
    private fun <T> getTableColumnInfos(clazz: Class<T>): List<ColumnInfo> {
        val fields = clazz.declaredFields
        val columnInfos = ArrayList<ColumnInfo>()
        for (field in fields) {

            if (field.name.contains("$")) {
                continue
            }


            if (!field.isAccessible)
                field.isAccessible = true
            var column = field.getAnnotation(Column::class.java)

            val columnInfo = ColumnInfo()
            columnInfo.name = field.name
            columnInfo.type = SqlHelper.getColumType(field.type.name)
            if (column != null) {
                columnInfo.isPrimaryKey = column.isPrimaryKey
                columnInfo.isUnique = column.isUnique
                columnInfo.isNull = column.isNull
            }

            columnInfos.add(columnInfo)
        }
        return columnInfos
    }

}